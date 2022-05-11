package com.fingerprintjs.android.aev.config

internal data class Config(

    // device id always needed

    // installed apps
    val installedAppsCollectionMode: InstalledAppsCollectionMode,

    // sensors
    val accelerometerSignalEnabled: Boolean,
    val accelerometerTimeoutMs: Long,
    val accelerometerValuesCountLimit: Int,
    val gyroscopeSignalEnabled: Boolean,
    val gyroscopeTimeoutMs: Long,
    val gyroscopeValuesCountLimit: Int,

    // userProfile. no need for disabling it since the signal will be sent anyway.
    // payload size difference is minimal

    // app info. no need for disabling it since the signal will be sent anyway, although
    // payload size difference might be more noticeable because of dataDir
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
            gyroscopeSignalEnabled = true,
            gyroscopeTimeoutMs = 1500,
            gyroscopeValuesCountLimit = 30
        )
    }
}
