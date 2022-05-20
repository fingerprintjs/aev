package com.fingerprintjs.android.aev


import com.fingerprintjs.android.aev.errors.ApiInteractorGetTokenError
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.requests.InitVerifyRequest
import com.fingerprintjs.android.aev.requests.InitVerifyResponse
import com.fingerprintjs.android.aev.signals.SignalProvider
import com.fingerprintjs.android.aev.transport.HttpClient
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap


internal interface ApiInteractor {
    fun getRequestId(
        signalProvider: SignalProvider
    ): Result<InitVerifyResponse, ApiInteractorGetTokenError>
}

internal class ApiInteractorImpl(
    private val httpClient: HttpClient,
    private val endpointURL: String,
    private val appId: String,
    private val logger: Logger,
    private val authorizationToken: String
) : ApiInteractor {

    override fun getRequestId(
        signalProvider: SignalProvider
    ): Result<InitVerifyResponse, ApiInteractorGetTokenError> {

        val fetchTokenRequest = InitVerifyRequest(
            endpointURL, appId, authorizationToken, signalProvider
        )

        val response = httpClient.performRequest(
            fetchTokenRequest
        )

        return response.flatMap { InitVerifyResponse.from(it) }
    }

}