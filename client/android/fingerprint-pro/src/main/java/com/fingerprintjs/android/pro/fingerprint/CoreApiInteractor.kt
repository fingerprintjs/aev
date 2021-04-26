package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.pro.fingerprint.events.FetchVisitorIdEvent
import com.fingerprintjs.android.pro.fingerprint.events.VisitorIdRequestResult
import com.fingerprintjs.android.pro.fingerprint.transport.EventSender


interface CoreApiInteractor {
    fun getVisitorId(
            deviceIdResult: DeviceIdResult,
            fingerprintResult: FingerprintResult,
            listener: (VisitorIdRequestResult) -> (Unit)
    )
}

class CoreApiInteractorImpl(
        private val eventSender: EventSender,
        private val token: String,
        private val appId: String,
) : CoreApiInteractor {
    override fun getVisitorId(
            deviceIdResult: DeviceIdResult,
            fingerprintResult: FingerprintResult,
            listener: (VisitorIdRequestResult) -> Unit
    ) {
        val hardwareSignalGroupProviderRawData = fingerprintResult.getSignalProvider(HardwareSignalGroupProvider::class.java)?.rawData()
                ?: return
        val osBuildSignalGroupProviderRawData = fingerprintResult.getSignalProvider(OsBuildSignalGroupProvider::class.java)?.rawData()
                ?: return
        val deviceStateSignalGroupProviderRawData = fingerprintResult.getSignalProvider(DeviceStateSignalGroupProvider::class.java)?.rawData()
                ?: return
        val installedAppsSignalGroupProviderRawData = fingerprintResult.getSignalProvider(InstalledAppsSignalGroupProvider::class.java)?.rawData()
                ?: return

        val visitorIdEvent = FetchVisitorIdEvent(
                appId,
                token,
                deviceIdResult,
                hardwareSignalGroupProviderRawData,
                osBuildSignalGroupProviderRawData,
                deviceStateSignalGroupProviderRawData,
                installedAppsSignalGroupProviderRawData
        )

        eventSender.send(visitorIdEvent) { requestResult ->
            listener.invoke(VisitorIdRequestResult(requestResult.rawResponse, requestResult.type, requestResult.rawResponse))
        }
    }
}