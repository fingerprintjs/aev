package com.fingerprintjs.android.pro.fingerprint.requests


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateRawData
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareFingerprintRawData
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsRawData
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildRawData
import com.fingerprintjs.android.pro.fingerprint.transport.Request
import com.fingerprintjs.android.pro.fingerprint.transport.RequestResult
import com.fingerprintjs.android.pro.fingerprint.transport.RequestResultType


class FetchTokenRequestResult(
        val token: String,
        type: RequestResultType,
        rawResponse: ByteArray?
) : RequestResult(type, rawResponse)


class FetchTokenRequest(
        appName: String,
        token: String,
        private val deviceIdResult: DeviceIdResult,
        private val hardwareRawData: HardwareFingerprintRawData,
        private val osBuildRawData: OsBuildRawData,
        private val deviceStateRawData: DeviceStateRawData,
        private val installedAppsRawData: InstalledAppsRawData
) : Request {

    override val path = "/"
    override val type = "POST"
    override val headers = mapOf(
            "appname" to appName,
            "Content-Type" to "application/json"
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()

        val deviceIdSignal = mapOf(
                STATE_KEY to "0",
                VALUE_KEY to mapOf(
                        ANDROID_ID_KEY to deviceIdResult.androidId,
                        GSF_ID_KEY to deviceIdResult.gsfId
                )
        )

        resultMap[DEVICE_ID_SECTION_KEY] = deviceIdSignal

        resultMap["a2"] = wrapSignalToMap("0", hardwareRawData.manufacturerName())
        resultMap["a3"] = wrapSignalToMap("0", hardwareRawData.modelName())
        resultMap["a4"] = wrapSignalToMap("0", hardwareRawData.totalRAM())
        resultMap["a5"] = wrapSignalToMap("0", hardwareRawData.totalInternalStorageSpace())
        resultMap["a6"] = wrapSignalToMap("0", hardwareRawData.procCpuInfo())
        resultMap["a7"] = wrapSignalToMap("0", hardwareRawData.sensors())
        resultMap["a8"] = wrapSignalToMap("0", hardwareRawData.inputDevices())
        resultMap["a9"] = wrapSignalToMap("0", hardwareRawData.batteryHealth())
        resultMap["a10"] = wrapSignalToMap("0", hardwareRawData.batteryFullCapacity())
        resultMap["a11"] = wrapSignalToMap("0", hardwareRawData.cameraList())
        resultMap["a12"] = wrapSignalToMap("0", hardwareRawData.glesVersion())
        resultMap["a13"] = wrapSignalToMap("0", hardwareRawData.abiType())
        resultMap["a14"] = wrapSignalToMap("0", hardwareRawData.coresCount())


        resultMap["a15"] = wrapSignalToMap("0", osBuildRawData.fingerprint())
        resultMap["a16"] = wrapSignalToMap("0", osBuildRawData.androidVersion())
        resultMap["a17"] = wrapSignalToMap("0", osBuildRawData.sdkVersion())
        resultMap["a18"] = wrapSignalToMap("0", osBuildRawData.kernelVersion())
        resultMap["a19"] = wrapSignalToMap("0", osBuildRawData.codecList())
        resultMap["a20"] = wrapSignalToMap("0", osBuildRawData.encryptionStatus())
        resultMap["a21"] = wrapSignalToMap("0", osBuildRawData.securityProviders())

        resultMap["a22"] = wrapSignalToMap("0", deviceStateRawData.adbEnabled())
        resultMap["a23"] = wrapSignalToMap("0", deviceStateRawData.developmentSettingsEnabled())
        resultMap["a24"] = wrapSignalToMap("0", deviceStateRawData.httpProxy())
        resultMap["a25"] = wrapSignalToMap("0", deviceStateRawData.transitionAnimationScale())
        resultMap["a26"] = wrapSignalToMap("0", deviceStateRawData.windowAnimationScale())
        resultMap["a27"] = wrapSignalToMap("0", deviceStateRawData.dataRoamingEnabled())
        resultMap["a28"] = wrapSignalToMap("0", deviceStateRawData.accessibilityEnabled())
        resultMap["a29"] = wrapSignalToMap("0", deviceStateRawData.defaultInputMethod())
        resultMap["a30"] = wrapSignalToMap("0", deviceStateRawData.rttCallingMode())
        resultMap["a31"] = wrapSignalToMap("0", deviceStateRawData.touchExplorationEnabled())
        resultMap["a32"] = wrapSignalToMap("0", deviceStateRawData.alarmAlertPath())
        resultMap["a33"] = wrapSignalToMap("0", deviceStateRawData.dateFormat())
        resultMap["a34"] = wrapSignalToMap("0", deviceStateRawData.endButtonBehaviour())
        resultMap["a35"] = wrapSignalToMap("0", deviceStateRawData.fontScale())
        resultMap["a36"] = wrapSignalToMap("0", deviceStateRawData.screenOffTimeout())
        resultMap["a37"] = wrapSignalToMap("0", deviceStateRawData.textAutoReplaceEnable())
        resultMap["a38"] = wrapSignalToMap("0", deviceStateRawData.textAutoPunctuate())
        resultMap["a39"] = wrapSignalToMap("0", deviceStateRawData.time12Or24())
        resultMap["a40"] = wrapSignalToMap("0", deviceStateRawData.isPinSecurityEnabled())
        resultMap["a41"] = wrapSignalToMap("0", deviceStateRawData.fingerprintSensorStatus())
        resultMap["a42"] = wrapSignalToMap("0", deviceStateRawData.ringtoneSource())
        resultMap["a43"] = wrapSignalToMap("0", deviceStateRawData.availableLocales())
        resultMap["a44"] = wrapSignalToMap("0", deviceStateRawData.regionCountry())
        resultMap["a45"] = wrapSignalToMap("0", deviceStateRawData.defaultLanguage())
        resultMap["a46"] = wrapSignalToMap("0", deviceStateRawData.timezone())

        resultMap["a47"] = wrapSignalToMap("0", installedAppsRawData.applicationsList())

        return resultMap
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
                            mapOf("codecName" to it.name,
                                    "codecCapabilities" to it.capabilities)
                        }
                        is InputDeviceData -> {
                            mapOf("vendor" to it.vendor, "name" to it.name)
                        }
                        is SensorData -> {
                            mapOf("sensorName" to it.sensorName,
                                    "vendorName" to it.vendorName)
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