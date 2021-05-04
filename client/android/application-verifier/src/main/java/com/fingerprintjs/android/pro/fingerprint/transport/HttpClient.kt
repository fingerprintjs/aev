package com.fingerprintjs.android.pro.fingerprint.transport


import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection


interface HttpClient {
    fun performRequest(
            type: String,
            url: String,
            headers: Map<String, String>,
            body: ByteArray?,
            responseListener: (RequestResult) -> (Unit)
    )
}

class OkHttpClientImpl : HttpClient {

    private val client = OkHttpClient()
    private val jsonSchema: MediaType = "application/json; charset=utf-8".toMediaType()

    override fun performRequest(
            type: String,
            url: String,
            headers: Map<String, String>,
            body: ByteArray?,
            responseListener: (RequestResult) -> Unit) {
        val requestResult = when (type) {
            "GET" -> {
                val request = Request.Builder()
                        .url(url)
                        .headers(headers.toHeaders())
                        .build()

                val response: Response = client.newCall(request).execute()
                RequestResult(RequestResultType.SUCCESS, response.body?.bytes())

                RequestResult(RequestResultType.SUCCESS, response.body?.bytes())

            }
            "POST" -> {
                val json = body?.toString(Charsets.UTF_8) ?: return
                val body: RequestBody = RequestBody.create(jsonSchema, json)
                val request = Request.Builder()
                        .url(url)
                        .headers(headers.toHeaders())
                        .post(body)
                        .build()

                try {
                    val response: Response = client.newCall(request).execute()
                    RequestResult(RequestResultType.SUCCESS, response.body?.bytes())
                } catch (e: Throwable) {
                    RequestResult(RequestResultType.ERROR, null)
                }
            }
            else -> {
                RequestResult(RequestResultType.ERROR, null)
            }
        }
        responseListener.invoke(requestResult)
    }
}


class HttpClientImpl : HttpClient {

    override fun performRequest(
            type: String,
            url: String,
            headers: Map<String, String>,
            body: ByteArray?,
            responseListener: (RequestResult) -> (Unit)) {
        var reader: BufferedReader? = null
        var stream: InputStream? = null
        var connection: HttpsURLConnection? = null

        try {
            val url = URL(url)
            connection = url.openConnection() as HttpsURLConnection

            headers.forEach {
                connection.setRequestProperty(it.key, it.value)
            }

            connection.requestMethod = type
            connection.readTimeout = 10000

            body?.let {
                connection.doOutput = true
                connection.setRequestProperty("Content-Length", it.size.toString())
                val writer = OutputStreamWriter(connection.outputStream, Charsets.UTF_8)
                writer.write(body.toString(Charsets.UTF_8))
            }


            stream = connection.inputStream
            reader = BufferedReader(InputStreamReader(stream, Charsets.UTF_8))

            responseListener.invoke(handleResponse(connection, stream, reader))

        } finally {
            reader?.close()
            stream?.close()
            connection?.disconnect()
        }
    }

    private fun handleResponse(connection: HttpsURLConnection,
                               stream: InputStream,
                               reader: BufferedReader
    ): RequestResult {
        return when (connection.responseCode) {
            HttpsURLConnection.HTTP_OK -> {
                val buf = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    buf.append(line).append("\n")
                }

                buf.toString()

                RequestResult(
                        RequestResultType.ERROR,
                        null
                )
            }
            else -> {
                RequestResult(
                        RequestResultType.ERROR,
                        null
                )
            }
        }
    }
}