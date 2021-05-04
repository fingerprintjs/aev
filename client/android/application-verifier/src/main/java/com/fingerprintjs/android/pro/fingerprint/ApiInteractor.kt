package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenRequest
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenRequestResult
import com.fingerprintjs.android.pro.fingerprint.transport.EventSender


interface ApiInteractor {
    fun getToken(
            deviceIdResult: DeviceIdResult,
            fingerprintResult: FingerprintResult,
            listener: (FetchTokenRequestResult) -> (Unit)
    )
}

class ApiInteractorImpl(
        private val eventSender: EventSender,
        private val token: String,
        private val appId: String,
) : ApiInteractor {
    override fun getToken(
            deviceIdResult: DeviceIdResult,
            fingerprintResult: FingerprintResult,
            listener: (FetchTokenRequestResult) -> Unit
    ) {
        val hardwareSignalGroupProviderRawData = fingerprintResult.getSignalProvider(HardwareSignalGroupProvider::class.java)?.rawData()
                ?: return
        val osBuildSignalGroupProviderRawData = fingerprintResult.getSignalProvider(OsBuildSignalGroupProvider::class.java)?.rawData()
                ?: return
        val deviceStateSignalGroupProviderRawData = fingerprintResult.getSignalProvider(DeviceStateSignalGroupProvider::class.java)?.rawData()
                ?: return
        val installedAppsSignalGroupProviderRawData = fingerprintResult.getSignalProvider(InstalledAppsSignalGroupProvider::class.java)?.rawData()
                ?: return

        val fetchTokenRequest = FetchTokenRequest(
                appId,
                token,
                deviceIdResult,
                hardwareSignalGroupProviderRawData,
                osBuildSignalGroupProviderRawData,
                deviceStateSignalGroupProviderRawData,
                installedAppsSignalGroupProviderRawData
        )

        eventSender.send(fetchTokenRequest) { requestResult ->
            listener.invoke(FetchTokenRequestResult(requestResult.rawResponse.toString(), requestResult.type, requestResult.rawResponse))
        }
    }
}