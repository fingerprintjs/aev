package com.fingerprintjs.android.pro.fingerprint.transport


import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.transport.jwt.JwtClient
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.lang.Exception
import okhttp3.Request as OkHttpRequest


class OkHttpClientImpl(
    private val logger: Logger,
    private val jwtClient: JwtClient
) : HttpClient {

    private val client = OkHttpClient()
    private val jsonSchema: MediaType = "application/json; charset=utf-8".toMediaType()

    override fun performRequest(
        request: Request
    ): RawRequestResult {
        logger.debug(this, "Performing ${request.type} request")
        logger.debug(this, "Headers:")
        logger.debug(this, JSONObject(request.headers))
        logger.debug(this, "Body:")
        logger.debug(this, JSONObject(request.bodyAsMap()))
        return when (request.type) {
            "GET" -> {
                val okHttpRequest = OkHttpRequest.Builder()
                    .url(request.url)
                    .headers(request.headers.toHeaders())
                    .build()

                val response: Response = client.newCall(okHttpRequest).execute()
                RawRequestResult(RequestResultType.SUCCESS, response.body?.bytes())
            }
            "POST" -> {
                val json = jwtClient.signJsonBody(request.bodyAsMap())
                val body: RequestBody = json.toRequestBody(jsonSchema)

                val okHttpRequest = OkHttpRequest.Builder()
                    .url(request.url)
                    .headers(request.headers.toHeaders())
                    .post(body)
                    .build()

                try {
                    val response: Response = client.newCall(okHttpRequest).execute()
                    RawRequestResult(RequestResultType.SUCCESS, response.body?.bytes())
                } catch (e: Exception) {
                    logger.error(this, e)
                    RawRequestResult(RequestResultType.ERROR, null)
                }
            }
            else -> {
                logger.error(this, "Unrecognized request type")
                RawRequestResult(RequestResultType.ERROR, null)
            }
        }
    }
}