package com.fingerprintjs.android.aev.config

import com.fingerprintjs.android.aev.BuildConfig
import com.fingerprintjs.android.aev.transport.HttpClientImpl

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

    val sslPinningConfig: HttpClientImpl.SSLPinningConfig
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
            sslPinningConfig = HttpClientImpl.SSLPinningConfig(
                pinnedCerts = if (!BuildConfig.DEBUG_CONST)
                    listOf(
                        HttpClientImpl.SSLPinningConfig.PinnedCertInfo(
                            positionInChain = 2,
                            subjPubKeySha256Base64 = "++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI="
                        )
                    )
                else emptyList()
            )
        )
    }
}
