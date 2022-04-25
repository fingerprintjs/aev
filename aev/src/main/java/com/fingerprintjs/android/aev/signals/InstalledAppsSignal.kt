package com.fingerprintjs.android.aev.signals


import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.aev.raw_signal_providers.CertificateInfo
import com.fingerprintjs.android.aev.raw_signal_providers.InstallTime


internal class InstalledAppInfo(
    val packageName: String,
    val signingCertificateInfo: CertificateInfo,
    val installTime: InstallTime?
)

internal class InstalledAppsData(
    val installedApps: List<InstalledAppInfo>
)

internal class InstalledAppsSignal(value: InstalledAppsData) :
    Signal<InstalledAppsData>(INSTALLED_APPS_NAME, value) {
    override fun toMap() = mapOf(
        VALUE_KEY to this.value.installedApps.map { appInfo ->
            listOfNotNull(
                PACKAGE_NAME_KEY to appInfo.packageName,
                DNAME_NAME_KEY to appInfo.signingCertificateInfo.dnameList,
                SIG_ALG_KEY to appInfo.signingCertificateInfo.sigAlgInfo,
                appInfo.installTime?.firstInstallTime?.let {
                    FIRST_INSTALL_TIMESTAMP_KEY to it
                }
            ).toMap()
        }
    )
}

private const val INSTALLED_APPS_NAME = "installedApps"
private const val PACKAGE_NAME_KEY = "packageName"
private const val DNAME_NAME_KEY = "dname"
private const val SIG_ALG_KEY = "sigAlg"
private const val FIRST_INSTALL_TIMESTAMP_KEY = "firstInstallTimestamp"

