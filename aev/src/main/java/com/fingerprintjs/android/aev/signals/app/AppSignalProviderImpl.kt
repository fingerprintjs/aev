package com.fingerprintjs.android.aev.signals.app

import android.content.Context
import android.content.pm.PackageManager

internal class AppSignalProviderImpl(
    private val context: Context
) : AppSignalProvider {

    override fun getAppSignal(): AppSignal? = runCatching {
        val packageManager = context.packageManager

        val appName = context.packageName
        val appInfo = packageManager.getApplicationInfo(appName, PackageManager.GET_META_DATA)
        val dataDir = appInfo.dataDir

        AppSignal(
            AppData(
                name = appName,
                dataDir = dataDir
            )
        )
    }.getOrNull()

}
