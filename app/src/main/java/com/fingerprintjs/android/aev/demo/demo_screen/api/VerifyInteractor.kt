package com.fingerprintjs.android.aev.demo.demo_screen.api


import com.fingerprintjs.android.aev.demo.ApplicationPreferences
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.transport.OkHttpClientImpl


interface VerifyInteractor {
    fun verify(requestId: String): VerificationResult
}

class VerifyInteractorImpl(
    private val applicationPreferences: ApplicationPreferences,
    private val logger: Logger
) : VerifyInteractor {
    override fun verify(requestId: String): VerificationResult {
        val endpointURL = applicationPreferences.getEndpointUrl()
        val privateApiKey = applicationPreferences.getPrivateApiKey()

        val verifyRequest = VerifyRequest(
            endpointURL,
            privateApiKey,
            requestId
        )
        val httpClient = OkHttpClientImpl(logger)
        val rawRequestResult = httpClient.performRequest(
            verifyRequest
        )

        val response =
            VerificationResultResponse(rawRequestResult.type, rawRequestResult.rawResponse)
        rawRequestResult.rawResponse?.let {
            logger.debug(this, "Response: ${String(it, Charsets.UTF_8)}")
        }
        return response.typedResult()!!
    }
}
