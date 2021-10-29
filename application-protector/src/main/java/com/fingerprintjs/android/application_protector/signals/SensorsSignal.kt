package com.fingerprintjs.android.application_protector.signals


import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.application_protector.raw_signal_providers.SensorsResult


class SensorsSignal(value: SensorsResult) : Signal<SensorsResult>(SENSORS_SIGNAL_NAME, value) {

    override fun toMap() = mapOf(
        VALUE_KEY to mapOf(
            ACCELEROMETER_KEY to value.accelerometerData,
            GYROSCOPE_KEY to value.gyroscopeData
        )
    )
}

private const val SENSORS_SIGNAL_NAME = "sensors"
private const val ACCELEROMETER_KEY = "accelerometer"
private const val GYROSCOPE_KEY = "gyroscope"
