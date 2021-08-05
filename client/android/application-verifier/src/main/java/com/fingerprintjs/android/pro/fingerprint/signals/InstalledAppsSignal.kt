package com.fingerprintjs.android.pro.fingerprint.signals

import com.fingerprintjs.android.fingerprint.signal_providers.Signal


class AppInfo(
    val packageName: String
)

class InstalledAppsData(
    val installedApps: List<AppInfo>
)

//172.17.36.149
class InstalledAppsSignal(value: InstalledAppsData) :
    Signal<InstalledAppsData>(INSTALLED_APPS_NAME, value) {
    override fun toMap() = mapOf(
        VALUE_KEY to this.value.installedApps.map {
            mapOf(PACKAGE_NAME_KEY to it.packageName)
        }
    )
}

private const val INSTALLED_APPS_NAME = "installedApps"
private const val PACKAGE_NAME_KEY = "packageName"