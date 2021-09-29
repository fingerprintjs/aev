package com.fingerprintjs.android.pro.fingerprint.raw_signal_providers

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.util.*
import java.util.concurrent.CountDownLatch


class SensorsResult(
    val accelerometerData: List<List<Float>>,
    val gyroscopeData: List<List<Float>>
)

interface SensorsDataCollector {
    fun collect(): SensorsResult
}

class SensorsDataCollectorImpl(private val sensorManager: SensorManager) : SensorsDataCollector {
    override fun collect(): SensorsResult {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        return SensorsResult(
            collectSensorInfo(accelerometer),
            collectSensorInfo(gyroscope)
        )
    }

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
        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_FASTEST)

        countdownLatch.await()
        sensorManager.unregisterListener(sensorListener)

        return sensorValues
    }
}

private const val NUMBER_OF_SENSOR_VALUES = 30
private const val TIMEOUT_IN_MILLIS = 3000L