package com.fingerprintjs.android.aev.config

import com.fingerprintjs.android.aev.transport.NativeHttpClient

internal data class Config(
    val installedAppsCollectionMode: InstalledAppsCollectionMode,

    val accelerometerSignalEnabled: Boolean,
    val accelerometerTimeoutMs: Long,
    val accelerometerValuesCountLimit: Int,
    val accelerometerSamplingPeriodUs: Int,
    val gyroscopeSignalEnabled: Boolean,
    val gyroscopeTimeoutMs: Long,
    val gyroscopeValuesCountLimit: Int,
    val gyroscopeSamplingPeriodUs: Int,

    val sslPinningConfig: NativeHttpClient.SSLPinningConfig
) {
    enum class InstalledAppsCollectionMode {
        ALL,
        SYSTEM,
        NONE;
    }

    companion object {

        val DEFAULT = Config(
            installedAppsCollectionMode = InstalledAppsCollectionMode.ALL,
            accelerometerSignalEnabled = true,
            accelerometerTimeoutMs = 1500,
            accelerometerValuesCountLimit = 30,
            accelerometerSamplingPeriodUs = 5000,
            gyroscopeSignalEnabled = true,
            gyroscopeTimeoutMs = 1500,
            gyroscopeValuesCountLimit = 30,
            gyroscopeSamplingPeriodUs = 5000,
            sslPinningConfig = NativeHttpClient.SSLPinningConfig(
                pinnedCerts = listOf(
                    NativeHttpClient.SSLPinningConfig.PinnedCertInfo(
                        positionInChain = 2,
                        subjPubKeySha256Base64 = "++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI="
                    )
                )
            )
        )
    }
}
