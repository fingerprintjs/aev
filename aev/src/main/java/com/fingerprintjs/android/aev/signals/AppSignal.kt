package com.fingerprintjs.android.aev.signals

import com.fingerprintjs.android.aev.raw_signal_providers.package_manager.AppMetaData
import com.fingerprintjs.android.fingerprint.signal_providers.Signal

internal class AppSignal(value: AppMetaData) :
    Signal<AppMetaData>(APP_SIGNAL_NAME, value) {

    override fun toMap() = mapOf(
        VALUE_KEY to listOfNotNull(
            APP_NAME_KEY to value.name,
            APP_DATA_DIR_KEY to value.dataDir,
        ).toMap()
    )
}

private const val APP_SIGNAL_NAME = "appInfo"
private const val APP_NAME_KEY = "appName"
private const val APP_DATA_DIR_KEY = "dataDir"