package com.fingerprintjs.android.pro.fingerprint.transport


import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class OkHttpClientImpl(private val logger: Logger) : HttpClient {

    private val client = OkHttpClient()
    private val jsonSchema: MediaType = "application/json; charset=utf-8".toMediaType()

    override fun performRequest(
            type: String,
            url: String,
            headers: Map<String, String>,
            body: ByteArray?): RawRequestResult {
        return when (type) {
            "GET" -> {
                val request = Request.Builder()
                        .url(url)
                        .headers(headers.toHeaders())
                        .build()

                val response: Response = client.newCall(request).execute()
                RawRequestResult(RequestResultType.SUCCESS, response.body?.bytes())

                RawRequestResult(RequestResultType.SUCCESS, response.body?.bytes())

            }
            "POST" -> {
                val json = body?.toString(Charsets.UTF_8)
                        ?: return RawRequestResult(RequestResultType.ERROR, null)
                val body: RequestBody = RequestBody.create(jsonSchema, json)
                val request = Request.Builder()
                        .url(url)
                        .headers(headers.toHeaders())
                        .post(body)
                        .build()

                try {
                    val response: Response = client.newCall(request).execute()
                    RawRequestResult(RequestResultType.SUCCESS, response.body?.bytes())
                } catch (e: Throwable) {
                    RawRequestResult(RequestResultType.ERROR, null)
                }
            }
            else -> {
                RawRequestResult(RequestResultType.ERROR, null)
            }
        }
    }
}