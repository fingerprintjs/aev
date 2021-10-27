package com.fingerprintjs.android.pro.playgroundpro.demo.api


import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.transport.OkHttpClientImpl
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences


interface GetResultsInteractor {
    fun results(token: String): VerificationResult
}

class GetResultsInteractorImpl(
    private val applicationPreferences: ApplicationPreferences,
    private val logger: Logger
) : GetResultsInteractor {
    override fun results(token: String): VerificationResult {
        val endpointURL = applicationPreferences.getEndpointUrl()
        val authorizationToken = applicationPreferences.getApiToken()

        val verifyTokenRequest = VerifyTokenRequest(
            endpointURL,
            authorizationToken,
            token
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
