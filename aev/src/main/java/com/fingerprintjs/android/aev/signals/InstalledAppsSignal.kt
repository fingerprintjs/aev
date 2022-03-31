package com.fingerprintjs.android.aev.signals


import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.aev.raw_signal_providers.CertificateInfo


internal class AppInfo(
    val packageName: String,
    val signingCertificateInfo: CertificateInfo
)

internal class InstalledAppsData(
    val installedApps: List<AppInfo>
)

internal class InstalledAppsSignal(value: InstalledAppsData) :
    Signal<InstalledAppsData>(INSTALLED_APPS_NAME, value) {
    override fun toMap() = mapOf(
        VALUE_KEY to this.value.installedApps.map {
            mapOf(
                PACKAGE_NAME_KEY to it.packageName,
                DNAME_NAME_KEY to it.signingCertificateInfo.dnameList,
                SIG_ALG_KEY to it.signingCertificateInfo.sigAlgInfo,
            )
        }
    )
}

private const val INSTALLED_APPS_NAME = "installedApps"
private const val PACKAGE_NAME_KEY = "packageName"
private const val DNAME_NAME_KEY = "dname"
private const val SIG_ALG_KEY = "sigAlg"

