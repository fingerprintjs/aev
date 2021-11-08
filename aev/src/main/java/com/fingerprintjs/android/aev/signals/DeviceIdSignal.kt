package com.fingerprintjs.android.aev.signals


import com.fingerprintjs.android.fingerprint.signal_providers.Signal


class DeviceIdData(
    val androidId: String,
    val gsfId: String?,
    val mediaDrmId: String?
)

class DeviceIdSignal(value: DeviceIdData): Signal<DeviceIdData>(DEVICE_ID_SIGNAL_NAME, value) {
    override fun toMap() = mapOf(
        VALUE_KEY to mapOf(
            ANDROID_ID_KEY to value.androidId,
            GSF_ID_KEY to value.gsfId,
            MEDIA_DRM_KEY to value.mediaDrmId
        )
    )
}

private const val DEVICE_ID_SIGNAL_NAME = "deviceIds"
private const val ANDROID_ID_KEY = "androidId"
private const val GSF_ID_KEY = "gsfId"
private const val MEDIA_DRM_KEY = "mediaDrmId"