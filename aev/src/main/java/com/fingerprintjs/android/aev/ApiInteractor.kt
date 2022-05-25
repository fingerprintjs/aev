package com.fingerprintjs.android.aev


import com.fingerprintjs.android.aev.errors.ApiInteractorInitVerifyError
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.requests.InitVerifyRequest
import com.fingerprintjs.android.aev.requests.InitVerifyResponse
import com.fingerprintjs.android.aev.signals.SignalProvider
import com.fingerprintjs.android.aev.transport.HttpClient
import com.cloned.github.michaelbull.result.Result
import com.cloned.github.michaelbull.result.flatMap


internal interface ApiInteractor {
    fun getRequestId(
        signalProvider: SignalProvider
    ): Result<InitVerifyResponse, ApiInteractorInitVerifyError>
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
    ): Result<InitVerifyResponse, ApiInteractorInitVerifyError> {

        val request = InitVerifyRequest(
            endpointURL, appId, authorizationToken, signalProvider
        )

        val response = httpClient.performRequest(
            request
        )

        return response.flatMap { InitVerifyResponse.from(it) }
    }

}