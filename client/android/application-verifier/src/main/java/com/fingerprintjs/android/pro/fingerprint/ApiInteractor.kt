package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenRequest
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenRequestResult
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenResponse
import com.fingerprintjs.android.pro.fingerprint.signals.Signal
import com.fingerprintjs.android.pro.fingerprint.signals.SignalProvider
import com.fingerprintjs.android.pro.fingerprint.transport.RequestPerformer


interface ApiInteractor {
    fun getToken(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult,
        signals: SignalProvider
    ): FetchTokenResponse?
}

class ApiInteractorImpl(
    private val requestPerformer: RequestPerformer,
    private val appId: String,
    private val logger: Logger
) : ApiInteractor {
    override fun getToken(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult,
        signals: SignalProvider
    ): FetchTokenResponse? {
        val hardwareSignalGroupProviderRawData =
            fingerprintResult.getSignalProvider(HardwareSignalGroupProvider::class.java)?.rawData()
                ?: return null
        val osBuildSignalGroupProviderRawData =
            fingerprintResult.getSignalProvider(OsBuildSignalGroupProvider::class.java)?.rawData()
                ?: return null
        val deviceStateSignalGroupProviderRawData =
            fingerprintResult.getSignalProvider(DeviceStateSignalGroupProvider::class.java)
                ?.rawData()
                ?: return null
        val installedAppsSignalGroupProviderRawData =
            fingerprintResult.getSignalProvider(InstalledAppsSignalGroupProvider::class.java)
                ?.rawData()
                ?: return null

        val fetchTokenRequest = FetchTokenRequest(
            appId,
            deviceIdResult,
            hardwareSignalGroupProviderRawData,
            osBuildSignalGroupProviderRawData,
            deviceStateSignalGroupProviderRawData,
            installedAppsSignalGroupProviderRawData
        )

        logger.debug(this, "Request formed")

        val requestResult = requestPerformer.perform(fetchTokenRequest)
        val response = FetchTokenRequestResult(requestResult.type, requestResult.rawResponse)
        requestResult.rawResponse?.let {
            logger.debug(this, String(it, Charsets.UTF_8))
        }
        return response.result()
    }
}