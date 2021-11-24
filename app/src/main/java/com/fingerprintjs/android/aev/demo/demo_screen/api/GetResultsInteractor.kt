package com.fingerprintjs.android.aev.demo.demo_screen.api


import com.fingerprintjs.android.aev.demo.ApplicationPreferences
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.transport.OkHttpClientImpl


interface GetResultsInteractor {
    fun results(requestId: String): VerificationResult
}

class GetResultsInteractorImpl(
    private val applicationPreferences: ApplicationPreferences,
    private val logger: Logger
) : GetResultsInteractor {
    override fun results(requestId: String): VerificationResult {
        val endpointURL = applicationPreferences.getEndpointUrl()
        val authorizationToken = applicationPreferences.getPrivateApiToken()

        val verifyTokenRequest = GetResultsRequest(
            endpointURL,
            authorizationToken,
            requestId
        )
        val httpClient = OkHttpClientImpl(logger)
        val rawRequestResult = httpClient.performRequest(
            verifyTokenRequest
        )

        val response =
            VerificationResultResponse(rawRequestResult.type, rawRequestResult.rawResponse)
        rawRequestResult.rawResponse?.let {
            logger.debug(this, "Response: ${String(it, Charsets.UTF_8)}")
        }
        return response.typedResult()!!
    }
}
