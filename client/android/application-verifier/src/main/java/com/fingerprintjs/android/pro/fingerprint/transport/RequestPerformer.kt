package com.fingerprintjs.android.pro.fingerprint.transport


import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import org.json.JSONObject


interface RequestPerformer {
    fun perform(request: Request): RawRequestResult
}

class RequestPerformerImpl(
    private val httpClient: HttpClient,
    private val endpointURL: String,
    private val logger: Logger
    // jwtClient
    // SSL pinning detector
) : RequestPerformer {

    override fun perform(request: Request): RawRequestResult {
        val requestBody = JSONObject(request.bodyAsMap()).toString().toByteArray()
        logger.debug(this, "Request body: ${JSONObject(request.bodyAsMap()).toString(2)}")
        // Calculate signatures and verify the host here

        return httpClient.performRequest(
            request.type,
            "$endpointURL${request.path}",
            request.headers,
            requestBody
        )
    }
}