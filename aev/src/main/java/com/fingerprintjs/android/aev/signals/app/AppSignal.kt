package com.fingerprintjs.android.aev.signals.app

import com.fingerprintjs.android.aev.signals.VALUE_KEY
import com.fingerprintjs.android.fingerprint.signal_providers.Signal

internal class AppSignal(value: AppData) :
    Signal<AppData>(APP_SIGNAL_NAME, value) {

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