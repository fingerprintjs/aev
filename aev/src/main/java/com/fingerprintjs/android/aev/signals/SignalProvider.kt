package com.fingerprintjs.android.aev.signals


import com.fingerprintjs.android.aev.raw_signal_providers.package_manager.PackageManagerInfoProvider
import com.fingerprintjs.android.aev.raw_signal_providers.SensorsDataCollector
import com.fingerprintjs.android.aev.raw_signal_providers.SensorsResult
import com.fingerprintjs.android.aev.raw_signal_providers.package_manager.AppMetaData
import com.fingerprintjs.android.aev.raw_signal_providers.user_manager.UserManagerInfoProvider
import com.fingerprintjs.android.aev.raw_signal_providers.user_manager.UserProfileInfo
import com.fingerprintjs.android.aev.utils.runInParallelVararg
import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider


internal interface SignalProvider {
    fun signals(): List<Signal<*>>
    fun deviceIdSignal(): Signal<DeviceIdData>
    fun installedAppsSignal(): Signal<InstalledAppsData>
    fun sensorsSignal(): Signal<SensorsResult>
    fun userProfileSignal(): UserProfileSignal?
    fun appSignal(): AppSignal?
}

internal class SignalProviderImpl private constructor(
    private val packageManagerInfoProvider: PackageManagerInfoProvider,
    private val sensorsDataCollector: SensorsDataCollector,
    private val deviceIdResult: DeviceIdResult,
    private val fingerprintResult: FingerprintResult,
    private val userManagerInfoProvider: UserManagerInfoProvider?,
    private val appName: String?
) : SignalProvider {
    override fun signals(): List<Signal<*>> {
        return runInParallelVararg(
            { deviceIdSignal() },
            { installedAppsSignal() },
            { sensorsSignal() },
            { userProfileSignal() },
            { appSignal() },
        )
            .mapNotNull { result -> result.getOrNull() }
    }

    override fun deviceIdSignal() = DeviceIdSignal(
        DeviceIdData(
            deviceIdResult.androidId,
            deviceIdResult.gsfId,
            deviceIdResult.mediaDrmId
        )
    )

    override fun installedAppsSignal(): Signal<InstalledAppsData> {
        val applicationsList = fingerprintResult
            .getSignalProvider(InstalledAppsSignalGroupProvider::class.java)
            ?.rawData()?.signals() ?: return InstalledAppsSignal(InstalledAppsData(emptyList()))

        return InstalledAppsSignal(
            InstalledAppsData(
                applicationsList[0].value.map {
                    InstalledAppInfo(
                        packageName = it.packageName,
                        signingCertificateInfo = packageManagerInfoProvider.getCertificateInfo(it.packageName),
                        installTime = packageManagerInfoProvider.getInstallTime(it.packageName),
                    )
                })
        )
    }

    override fun sensorsSignal(): Signal<SensorsResult> {
        val sensorsResult = sensorsDataCollector.collect()
        return SensorsSignal(sensorsResult)
    }

    override fun userProfileSignal(): UserProfileSignal {
        return UserProfileSignal(
            value = userManagerInfoProvider?.getUserProfileInfo()
                ?: UserProfileInfo(
                    userProfilesCount = null,
                    isManagedProfile = null,
                    isSystemUser = null
                )
        )
    }

    override fun appSignal(): AppSignal {
        return AppSignal(
            value = appName?.let { packageManagerInfoProvider.getAppMetaData(it) }
                ?: AppMetaData(
                    name = null,
                    dataDir = null
                )
        )
    }

    class SignalProviderBuilder(
        private val packageManagerInfoProvider: PackageManagerInfoProvider,
        private val sensorsDataCollector: SensorsDataCollector,
        private val userManagerInfoProvider: UserManagerInfoProvider?,
        private val appName: String?
    ) {
        private lateinit var fingerprintResult: FingerprintResult
        private lateinit var deviceIdResult: DeviceIdResult

        fun withFingerprintResult(fingerprintResult: FingerprintResult): SignalProviderBuilder {
            this.fingerprintResult = fingerprintResult
            return this
        }

        fun withDeviceIdResult(deviceIdResult: DeviceIdResult): SignalProviderBuilder {
            this.deviceIdResult = deviceIdResult
            return this
        }

        fun build(): SignalProvider {
            return SignalProviderImpl(
                packageManagerInfoProvider,
                sensorsDataCollector,
                deviceIdResult,
                fingerprintResult,
                userManagerInfoProvider,
                appName
            )
        }
    }
}