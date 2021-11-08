package com.fingerprintjs.android.aev.transport


import com.fingerprintjs.android.aev.logger.Logger
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
    private val logger: Logger
) : HttpClient {

    private val client = OkHttpClient()
    private val jsonSchema: MediaType = MEDIA_TYPE.toMediaType()

    override fun performRequest(
        request: Request
    ): RawRequestResult {
        logger.debug(this, "Performing ${request.type} request")
        logger.debug(this, "Headers:")
        logger.debug(this, JSONObject(request.headers))


        val okHttpRequest = when (request.type) {
            RequestType.GET -> {
                OkHttpRequest.Builder()
                    .url(request.url)
                    .headers(request.headers.toHeaders())
                    .build()
            }
            RequestType.POST -> {
                logger.debug(this, "Body:")
                logger.debug(this, JSONObject(request.bodyAsMap()))

                val json = JSONObject(request.bodyAsMap()).toString()
                val body: RequestBody = json.toRequestBody(jsonSchema)

                OkHttpRequest.Builder()
                    .url(request.url)
                    .headers(request.headers.toHeaders())
                    .post(body)
                    .build()
            }
        }

        return try {
            val response: Response = client.newCall(okHttpRequest).execute()
            RawRequestResult(RequestResultType.SUCCESS, response.body?.bytes())
        } catch (e: Exception) {
            logger.error(this, e)
            RawRequestResult(RequestResultType.ERROR, e.localizedMessage?.toByteArray())
        }
    }
}

private const val MEDIA_TYPE = "application/json; charset=utf-8"