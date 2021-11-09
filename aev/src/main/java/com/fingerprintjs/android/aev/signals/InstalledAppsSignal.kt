package com.fingerprintjs.android.aev.signals

import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.aev.raw_signal_providers.CertificateInfo


class AppInfo(
    val packageName: String,
    val signingCertificateInfo: CertificateInfo
)

class InstalledAppsData(
    val installedApps: List<AppInfo>
)

class InstalledAppsSignal(value: InstalledAppsData) :
    Signal<InstalledAppsData>(INSTALLED_APPS_NAME, value) {
    override fun toMap() = mapOf(
        VALUE_KEY to this.value.installedApps.map {
            mapOf(
                PACKAGE_NAME_KEY to it.packageName,
                DNAME_NAME_KEY to it.signingCertificateInfo.dnameList
            )
        }
    )
}

private const val INSTALLED_APPS_NAME = "installedApps"
private const val PACKAGE_NAME_KEY = "packageName"
private const val DNAME_NAME_KEY = "dname"