package com.fingerprintjs.android.aev.signals


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.aev.raw_signal_providers.PackageManagerInfoProvider
import com.fingerprintjs.android.aev.raw_signal_providers.SensorsDataCollector
import com.fingerprintjs.android.aev.raw_signal_providers.SensorsResult


interface SignalProvider {
    fun signals(): List<Signal<*>>
    fun deviceIdSignal(): Signal<DeviceIdData>
    fun installedAppsSignal(): Signal<InstalledAppsData>
    fun sensorsSignal(): Signal<SensorsResult>
}

class SignalProviderImpl private constructor(
    private val packageManagerInfoProvider: PackageManagerInfoProvider,
    private val sensorsDataCollector: SensorsDataCollector,
    private val deviceIdResult: DeviceIdResult,
    private val fingerprintResult: FingerprintResult
) : SignalProvider {
    override fun signals(): List<Signal<*>> {
        return listOf(
            deviceIdSignal(),
            installedAppsSignal(),
            sensorsSignal()
        )
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
                    AppInfo(
                        it.packageName,
                        packageManagerInfoProvider.getCertificateInfo(it.packageName)
                    )
                })
        )
    }

    override fun sensorsSignal(): Signal<SensorsResult> {
        val sensorsResult = sensorsDataCollector.collect()
        return SensorsSignal(sensorsResult)
    }

    class SignalProviderBuilder(
        private val packageManagerInfoProvider: PackageManagerInfoProvider,
        private val sensorsDataCollector: SensorsDataCollector
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
                fingerprintResult
            )
        }
    }
}