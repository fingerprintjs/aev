package com.fingerprintjs.android.aev.raw_signal_providers


import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.fingerprintjs.android.aev.config.Config
import com.fingerprintjs.android.aev.utils.concurrency.runInParallel
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import com.cloned.github.michaelbull.result.getOr
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


internal class SensorsResult(
    val accelerometerData: List<List<Float>>,
    val gyroscopeData: List<List<Float>>
)

internal interface SensorsDataCollector {
    fun collect(): SensorsResult
}

internal class SensorsDataCollectorImpl(
    private val sensorManager: SensorManager,
    private val config: Config
) : SensorsDataCollector {
    override fun collect() = executeSafe({
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        runInParallel(
            {
                if (config.accelerometerSignalEnabled)
                    collectSensorInfo(
                        sensor = accelerometer,
                        valuesCountLimit = config.accelerometerValuesCountLimit,
                        timeoutMs = config.accelerometerTimeoutMs,
                        samplingPeriodUs = config.accelerometerSamplingPeriodUs,
                    )
                else emptyList()
            },
            {
                if (config.gyroscopeSignalEnabled)
                    collectSensorInfo(
                        sensor = gyroscope,
                        valuesCountLimit = config.gyroscopeValuesCountLimit,
                        timeoutMs = config.gyroscopeTimeoutMs,
                        samplingPeriodUs = config.gyroscopeSamplingPeriodUs,
                    )
                else emptyList()
            },
        ).let { pair ->
            SensorsResult(
                pair.first.getOr(emptyList()),
                pair.second.getOr(emptyList()),
            )
        }
    }, SensorsResult(emptyList(), emptyList()))

    private fun collectSensorInfo(
        sensor: Sensor?,
        valuesCountLimit: Int,
        timeoutMs: Long,
        samplingPeriodUs: Int,
    ): List<List<Float>> {
        if (sensor == null) return emptyList()
        val countdownLatch = CountDownLatch(valuesCountLimit)
        val sensorValues = LinkedList<List<Float>>()

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent?) {
                countdownLatch.countDown()
                val values = p0?.values ?: return
                sensorValues.add(listOf(values[0], values[1], values[2]))
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                // Do nothing
            }

        }
        sensorManager.registerListener(sensorListener, sensor, samplingPeriodUs)

        try {
            countdownLatch.await(timeoutMs, TimeUnit.MILLISECONDS)
        } catch (exception: InterruptedException) {
            // Continue execution
        }
        sensorManager.unregisterListener(sensorListener)

        return sensorValues
    }
}

internal class SensorsDataCollectorBuilder(
    private val sensorManager: SensorManager,
) {
    private var config: Config = Config.DEFAULT

    fun withConfig(config: Config): SensorsDataCollectorBuilder {
        this.config = config
        return this
    }

    fun build(): SensorsDataCollector {
        return SensorsDataCollectorImpl(
            sensorManager = sensorManager,
            config = config
        )
    }
}
