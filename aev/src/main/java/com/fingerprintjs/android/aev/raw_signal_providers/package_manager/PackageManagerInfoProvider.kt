package com.fingerprintjs.android.aev.raw_signal_providers.package_manager


internal interface PackageManagerInfoProvider {
    fun getCertificateInfo(packageName: String): CertificateInfo
    fun getInstallTime(packageName: String): InstallTime?
    fun getAppMetaData(packageName: String): AppMetaData
}
