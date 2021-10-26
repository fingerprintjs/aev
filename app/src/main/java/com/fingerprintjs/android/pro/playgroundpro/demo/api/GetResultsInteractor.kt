package com.fingerprintjs.android.pro.playgroundpro.demo.api


import com.fingerprintjs.android.pro.fingerprint.transport.HttpClient
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences


interface GetResultsInteractor {
    fun results(token: String): VerificationResult
}

class GetResultsInteractorImpl(
    private val httpClient: HttpClient,
    private val applicationPreferences: ApplicationPreferences
) : GetResultsInteractor {
    override fun results(token: String): VerificationResult {
        val endpointURL = applicationPreferences.getEndpointUrl()
        val authorizationToken = applicationPreferences.getApiToken()

        val verifyTokenRequest = VerifyTokenRequest(
            endpointURL,
            authorizationToken,
            token
        )

        val rawRequestResult = httpClient.performRequest(
            verifyTokenRequest
        )

        val response = VerificationResultResponse(rawRequestResult.type, rawRequestResult.rawResponse)
        rawRequestResult.rawResponse?.let {
            print("Response: ${String(it, Charsets.UTF_8)}")
        }
        return response.typedResult()!!
    }
}
