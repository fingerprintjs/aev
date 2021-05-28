package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenRequest
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenRequestResult
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenResponse
import com.fingerprintjs.android.pro.fingerprint.transport.HttpClient
import com.fingerprintjs.android.pro.fingerprint.transport.RequestResultType
import com.fingerprintjs.android.pro.fingerprint.transport.ssl.SSLConnectionInspector


interface ApiInteractor {
    fun getToken(
        signals: List<Signal<*>>
    ): FetchTokenResponse
}

class ApiInteractorImpl(
    private val httpClient: HttpClient,
    private val endpointURL: String,
    private val appId: String,
    private val logger: Logger,
    private val sslConnectionInspector: SSLConnectionInspector
) : ApiInteractor {
    override fun getToken(
        signals: List<Signal<*>>
    ): FetchTokenResponse {

        if (!sslConnectionInspector.inspectConnection(endpointURL)) {
            return FetchTokenRequestResult(RequestResultType.ERROR, null).typedResult()
        }
        
        val fetchTokenRequest = FetchTokenRequest(
            endpointURL, appId, signals
        )

        val rawRequestResult = httpClient.performRequest(
            fetchTokenRequest
        )

        val response = FetchTokenRequestResult(rawRequestResult.type, rawRequestResult.rawResponse)
        rawRequestResult.rawResponse?.let {
            logger.debug(this, "Response: ${String(it, Charsets.UTF_8)}")
        }
        return response.typedResult()
    }
}