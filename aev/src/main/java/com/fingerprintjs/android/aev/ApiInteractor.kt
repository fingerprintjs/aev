package com.fingerprintjs.android.aev


import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.requests.FetchRequestId
import com.fingerprintjs.android.aev.requests.FetchRequestIdResult
import com.fingerprintjs.android.aev.requests.FetchRequestIdResponse
import com.fingerprintjs.android.aev.signals.SignalProvider
import com.fingerprintjs.android.aev.transport.HttpClient


interface ApiInteractor {
    fun getRequestId(
        signalProvider: SignalProvider
    ): FetchRequestIdResponse
}

class ApiInteractorImpl(
    private val httpClient: HttpClient,
    private val endpointURL: String,
    private val appId: String,
    private val logger: Logger,
    private val authorizationToken: String
) : ApiInteractor {
    override fun getRequestId(
        signalProvider: SignalProvider
    ): FetchRequestIdResponse {

        val fetchTokenRequest = FetchRequestId(
            endpointURL, appId, authorizationToken, signalProvider
        )

        val rawRequestResult = httpClient.performRequest(
            fetchTokenRequest
        )

        val response = FetchRequestIdResult(rawRequestResult.type, rawRequestResult.rawResponse)
        rawRequestResult.rawResponse?.let {
            logger.debug(this, "Response: ${String(it, Charsets.UTF_8)}")
        }
        return response.typedResult()
    }
}