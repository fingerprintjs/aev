package com.fingerprintjs.android.aev.raw_signal_providers


import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class SensorsResult(
    val accelerometerData: List<List<Float>>,
    val gyroscopeData: List<List<Float>>
)

interface SensorsDataCollector {
    fun collect(): SensorsResult
}

class SensorsDataCollectorImpl(private val sensorManager: SensorManager) : SensorsDataCollector {
    override fun collect() = executeSafe({
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        SensorsResult(
            collectSensorInfo(accelerometer),
            collectSensorInfo(gyroscope)
        )
    }, SensorsResult(emptyList(), emptyList()))

    private fun collectSensorInfo(
        sensor: Sensor?
    ): List<List<Float>> {
        if (sensor == null) return emptyList()
        val countdownLatch = CountDownLatch(NUMBER_OF_SENSOR_VALUES)
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
        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_UI)

        try {
            countdownLatch.await(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
        } catch (exception: InterruptedException) {
            // Continue execution
        }
        sensorManager.unregisterListener(sensorListener)

        return sensorValues
    }
}

private const val NUMBER_OF_SENSOR_VALUES = 30
private const val TIMEOUT_IN_MILLIS = 1500L