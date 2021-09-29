package com.fingerprintjs.android.pro.fingerprint.signals


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.device_id.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.pro.fingerprint.raw_signal_providers.*
import com.fingerprintjs.android.pro.fingerprint.signals.filesystem.MountedPathsSignal
import com.fingerprintjs.android.pro.fingerprint.signals.filesystem.SuBinarySignal
import com.fingerprintjs.android.pro.fingerprint.signals.filesystem.WhichCallSignal
import com.fingerprintjs.android.pro.fingerprint.signals.os_build.BuildTagsSignal
import java.util.LinkedList


interface SignalProvider {
    fun signals(): List<Signal<*>>
    fun deviceIdSignal(): Signal<DeviceIdData>
    fun installedAppsSignal(): Signal<InstalledAppsData>
    fun sensorsSignal(): Signal<SensorsResult>
}

class SignalProviderImpl private constructor(
    private val mountedPathsReader: MountedPathsReader,
    private val suChecker: SuChecker,
    private val packageManagerInfoProvider: PackageManagerInfoProvider,
    private val sensorsDataCollector: SensorsDataCollector,
    private val deviceIdResult: DeviceIdResult,
    private val fingerprintResult: FingerprintResult
) : SignalProvider {
    override fun signals(): List<Signal<*>> {
        val signals = LinkedList<Signal<*>>()

        val hardwareSignals = fingerprintResult
            .getSignalProvider(HardwareSignalGroupProvider::class.java)
            ?.rawData()
            ?.signals() ?: emptyList()

        val osBuildSignals = fingerprintResult
            .getSignalProvider(OsBuildSignalGroupProvider::class.java)
            ?.rawData()
            ?.signals() ?: emptyList()

        val deviceStateSignals = fingerprintResult
            .getSignalProvider(DeviceStateSignalGroupProvider::class.java)
            ?.rawData()
            ?.signals() ?: emptyList()

        val instaledAppsSignals = fingerprintResult
            .getSignalProvider(InstalledAppsSignalGroupProvider::class.java)
            ?.rawData()
            ?.signals() ?: emptyList()

        val deviceIdSignals = fingerprintResult
            .getSignalProvider(DeviceIdProvider::class.java)
            ?.rawData()
            ?.signals() ?: emptyList()

        signals.apply {
            addAll(hardwareSignals)
            addAll(osBuildSignals)
            addAll(deviceStateSignals)
            addAll(instaledAppsSignals)
            addAll(deviceIdSignals)
            add(MountedPathsSignal(mountedPathsReader.getMountedPaths()))
            add(SuBinarySignal(suChecker.foundSuBinaries()))
            add(WhichCallSignal(suChecker.resultOfWhich()))
            add(BuildTagsSignal())
        }

        return signals
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
                    AppInfo(it.packageName, packageManagerInfoProvider.getCertificateInfo(it.packageName))
                })
        )
    }

    override fun sensorsSignal(): Signal<SensorsResult> {
        val sensorsResult = sensorsDataCollector.collect()
        return SensorsSignal(sensorsResult)
    }

    class SignalProviderBuilder(
        private val mountedPathsReader: MountedPathsReader,
        private val suChecker: SuChecker,
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
                mountedPathsReader,
                suChecker,
                packageManagerInfoProvider,
                sensorsDataCollector,
                deviceIdResult,
                fingerprintResult
            )
        }
    }
}