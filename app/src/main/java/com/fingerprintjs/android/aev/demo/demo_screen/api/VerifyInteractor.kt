package com.fingerprintjs.android.aev.demo.demo_screen.api


import com.fingerprintjs.android.aev.demo.ApplicationPreferences
import com.fingerprintjs.android.aev.demo.utils.network.NativeHttpClient


interface VerifyInteractor {
    fun verify(requestId: String): VerificationResult
}

class VerifyInteractorImpl(
    private val applicationPreferences: ApplicationPreferences,
    private val logger: NativeHttpClient.Logger
) : VerifyInteractor {
    override fun verify(requestId: String): VerificationResult {
        val endpointURL = applicationPreferences.getEndpointUrl()
        val privateApiKey = applicationPreferences.getPrivateApiKey()

        val verifyRequest = VerifyRequest(
            endpointURL,
            privateApiKey,
            requestId
        )
        val httpClient = NativeHttpClient(logger)
        val rawRequestResult = httpClient.performRequest(
            verifyRequest
        )

        return VerificationResultResponse(
            rawRequestResult.type,
            rawRequestResult.rawResponse
        ).typedResult()!!
    }
}
