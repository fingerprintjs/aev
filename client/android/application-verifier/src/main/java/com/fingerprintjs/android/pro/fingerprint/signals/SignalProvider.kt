package com.fingerprintjs.android.pro.fingerprint.signals


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.pro.fingerprint.requests.STATE_KEY
import com.fingerprintjs.android.pro.fingerprint.requests.VALUE_KEY
import java.util.*


interface SignalProvider {
    fun signals(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult
    ): List<Signal<*>>
}

class SignalProviderImpl() : SignalProvider {
    override fun signals(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult
    ): List<VerificationSignal<*>> {
        val signals = LinkedList<VerificationSignal<*>>()

        val identificationHardwareSignals = fingerprintResult
            .getSignalProvider(HardwareSignalGroupProvider::class.java)
            ?.rawData()
            ?.signals()
            ?.map {
                object : VerificationSignal<Any>(
                    it as Signal<Any>
                ) {
                    override fun toMap() = wrapSignalToMap("0", it)
                    override fun toString() = ""
                }
            } ?: emptyList()

        signals.addAll(identificationHardwareSignals)

        return signals
    }

    private fun wrapSignalToMap(state: String, signal: Signal<*>): Map<String, Any> {
        return when (val value = signal.value ?: emptyMap<String, Any>()) {
            is String -> mapOf(
                STATE_KEY to state,
                VALUE_KEY to value
            )
            is Int -> mapOf(
                STATE_KEY to state,
                VALUE_KEY to value
            )
            is Long -> mapOf(
                STATE_KEY to state,
                VALUE_KEY to value
            )
            is Boolean -> mapOf(
                STATE_KEY to state,
                VALUE_KEY to value
            )
            is Map<*, *> -> mapOf(
                STATE_KEY to state,
                VALUE_KEY to value
            )
            is List<*> -> {
                val listValue = value.map {
                    when (it) {
                        is MediaCodecInfo -> {
                            val sb = StringBuilder()
                            mapOf(
                                "codecName" to it.name,
                                "codecCapabilities" to it.capabilities
                            )
                        }
                        is InputDeviceData -> {
                            mapOf("vendor" to it.vendor, "name" to it.name)
                        }
                        is SensorData -> {
                            mapOf(
                                "sensorName" to it.sensorName,
                                "vendorName" to it.vendorName
                            )
                        }
                        is CameraInfo -> {
                            mapOf(
                                "cameraName" to it.cameraName,
                                "cameraType" to it.cameraType,
                                "cameraOrientation" to it.cameraOrientation
                            )
                        }
                        is Pair<*, *> -> listOf(
                            it.first.toString(),
                            it.second.toString()
                        )
                        else -> {
                            it.toString()
                        }
                    }
                }
                mapOf(
                    STATE_KEY to state,
                    VALUE_KEY to listValue
                )
            }
            else -> {
                mapOf(
                    STATE_KEY to state,
                    VALUE_KEY to value.toString()
                )
            }
        }
    }

}