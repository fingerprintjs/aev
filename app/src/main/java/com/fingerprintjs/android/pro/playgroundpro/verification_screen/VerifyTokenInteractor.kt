package com.fingerprintjs.android.pro.playgroundpro.verification_screen


import com.fingerprintjs.android.pro.fingerprint.transport.HttpClient


interface VerifyTokenInteractor {
    fun verifyToken(token: String): VerificationResult
}

class VerifyTokenInteractorImpl(
    private val httpClient: HttpClient,
    private val endpointURL: String,
    private val authorizationToken: String
) : VerifyTokenInteractor {
    override fun verifyToken(token: String): VerificationResult {
        val verifyTokenRequest = VerifyTokenRequest(
            endpointURL,
            authorizationToken,
            token
        )

        val rawRequestResult = httpClient.performRequest(
            verifyTokenRequest
        )

        val response = VerifyTokenResponse(rawRequestResult.type, rawRequestResult.rawResponse)
        rawRequestResult.rawResponse?.let {
            print("Response: ${String(it, Charsets.UTF_8)}")
        }
        return response.typedResult()!!
    }
}
