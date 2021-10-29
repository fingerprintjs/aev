package com.fingerprintjs.android.application_protector


import com.fingerprintjs.android.application_protector.logger.Logger
import com.fingerprintjs.android.application_protector.requests.FetchTokenRequest
import com.fingerprintjs.android.application_protector.requests.FetchTokenRequestResult
import com.fingerprintjs.android.application_protector.requests.FetchTokenResponse
import com.fingerprintjs.android.application_protector.signals.SignalProvider
import com.fingerprintjs.android.application_protector.transport.HttpClient


interface ApiInteractor {
    fun getToken(
        signalProvider: SignalProvider
    ): FetchTokenResponse
}

class ApiInteractorImpl(
    private val httpClient: HttpClient,
    private val endpointURL: String,
    private val appId: String,
    private val logger: Logger,
    private val authorizationToken: String
) : ApiInteractor {
    override fun getToken(
        signalProvider: SignalProvider
    ): FetchTokenResponse {

        val fetchTokenRequest = FetchTokenRequest(
            endpointURL, appId, authorizationToken, signalProvider
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