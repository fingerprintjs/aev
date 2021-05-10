package com.fingerprintjs.android.pro.fingerprint.transport


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
            body: ByteArray?
    ): RawRequestResult
}

class HttpClientImpl : HttpClient {

    override fun performRequest(
            type: String,
            urlString: String,
            headers: Map<String, String>,
            body: ByteArray?): RawRequestResult {
        var reader: BufferedReader? = null
        var stream: InputStream? = null
        var connection: HttpsURLConnection? = null

        try {
            val url = URL(urlString)
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

            return handleResponse(connection, reader)

        } finally {
            reader?.close()
            stream?.close()
            connection?.disconnect()
        }
    }

    private fun handleResponse(connection: HttpsURLConnection,
                               reader: BufferedReader
    ): RawRequestResult {
        return when (connection.responseCode) {
            HttpsURLConnection.HTTP_OK -> {
                val buf = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    buf.append(line).append("\n")
                }

                RawRequestResult(
                        RequestResultType.ERROR,
                        buf.toString().toByteArray()
                )
            }
            else -> {
                RawRequestResult(
                        RequestResultType.ERROR,
                        null
                )
            }
        }
    }
}