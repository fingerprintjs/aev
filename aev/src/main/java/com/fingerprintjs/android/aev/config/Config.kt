package com.fingerprintjs.android.aev.config

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
        )
    }
}
