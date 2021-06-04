package com.fingerprintjs.android.pro.fingerprint.signals


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.device_id.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.pro.fingerprint.raw_signal_providers.MountedPathsReader
import com.fingerprintjs.android.pro.fingerprint.raw_signal_providers.SuChecker
import com.fingerprintjs.android.pro.fingerprint.signals.filesystem.MountedPathsSignal
import com.fingerprintjs.android.pro.fingerprint.signals.filesystem.SuBinarySignal
import com.fingerprintjs.android.pro.fingerprint.signals.filesystem.WhichCallSignal
import com.fingerprintjs.android.pro.fingerprint.signals.os_build.BuildTagsSignal
import java.util.LinkedList


interface SignalProvider {
    fun signals(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult
    ): List<Signal<*>>
}

class SignalProviderImpl(
    private val mountedPathsReader: MountedPathsReader,
    private val suChecker: SuChecker
) : SignalProvider {
    override fun signals(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult
    ): List<Signal<*>> {
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



}