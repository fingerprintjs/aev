package com.fingerprintjs.android.aev


import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.requests.InitVerifyRequest
import com.fingerprintjs.android.aev.requests.InitVerifyResult
import com.fingerprintjs.android.aev.requests.InitVerifyResponse
import com.fingerprintjs.android.aev.signals.SignalProvider
import com.fingerprintjs.android.aev.transport.HttpClient


internal interface ApiInteractor {
    fun getToken(
        signalProvider: SignalProvider
    ): InitVerifyResponse
}

internal class ApiInteractorImpl(
    private val httpClient: HttpClient,
    private val endpointURL: String,
    private val appId: String,
    private val logger: Logger,
    private val authorizationToken: String
) : ApiInteractor {
    override fun getToken(
        signalProvider: SignalProvider
    ): InitVerifyResponse {

        val fetchTokenRequest = InitVerifyRequest(
            endpointURL, appId, authorizationToken, signalProvider
        )

        val rawRequestResult = httpClient.performRequest(
            fetchTokenRequest
        )

        return InitVerifyResult(rawRequestResult.type, rawRequestResult.rawResponse).typedResult()
    }
}