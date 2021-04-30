package com.fingerprintjs.android.pro.fingerprint.transport


import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection


interface HttpClient {
    fun performHttpUrlRequest(
            type: String,
            url: String,
            headers: Map<String, String>,
            requestBody: String? = null
    ): String
}

class HttpClientImpl() : HttpClient {
    override fun performHttpUrlRequest(
            type: String,
            url: String,
            headers: Map<String, String>,
            requestBody: String?
    ): String {

        var reader: BufferedReader? = null
        var stream: InputStream? = null
        var connection: HttpsURLConnection? = null

        return try {
            val url = URL(url)
            connection = url.openConnection() as HttpsURLConnection

            headers.forEach {
                connection.setRequestProperty(it.key, it.value)
            }

            connection.requestMethod = type
            connection.readTimeout = 10000

            requestBody?.let {
                connection.doOutput = true
                connection.setRequestProperty("Content-Length", it.toByteArray(Charsets.UTF_8).size.toString())
                val writer = OutputStreamWriter(connection.outputStream, Charsets.UTF_8)
                writer.write(requestBody)
            }

            connection.connect()

            stream = connection.inputStream
            reader = BufferedReader(InputStreamReader(stream, Charsets.UTF_8))

            val buf = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                buf.append(line).append("\n")
            }

            buf.toString()

        } finally {
            reader?.close()
            stream?.close()
            connection?.disconnect()
        }
    }
}