package com.fingerprintjs.android.pro.fingerprint.transport


import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection


interface HttpClient {
    fun performRequest(responseListener: (RequestResult) -> (Unit))
}

class HttpClientImpl private constructor(
        private val type: String,
        private val url: String,
        private val headers: Map<String, String>,
        private val body: ByteArray?
) : HttpClient {

    override fun performRequest(responseListener: (RequestResult) -> (Unit)) {
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
        return  when (connection.responseCode) {
            HttpsURLConnection.HTTP_OK -> {
                val buf = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    buf.append(line).append("\n")
                }

                buf.toString()

                RequestResult(

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

    class Builder {
        private var type: String = "GET"
        private lateinit var url: String
        private var headers: Map<String, String> = emptyMap()
        private var body: ByteArray? = null

        fun build(): HttpClient {
            return HttpClientImpl(
                    type,
                    url,
                    headers,
                    body
            )
        }

        fun withType(type: String): Builder {
            this.type = type
            return this
        }

        fun withUrl(url: String): Builder {
            this.url = url
            return this
        }

        fun withHeaders(headers: Map<String, String>): Builder {
            this.headers = headers
            return this
        }

        fun withBody(body: ByteArray): Builder {
            this.body = body
            return this
        }
    }
}