package com.fingerprintjs.android.aev.demo.utils.network

import com.fingerprintjs.android.fingerprint.tools.executeSafe
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class NativeHttpClient(
    private val logger: Logger
) {
    fun performRequest(request: Request): RawRequestResult {
        return executeSafe({
            sendPostRequest(request)
        }, RawRequestResult(RequestResultType.ERROR, "Network error".toByteArray()))
    }

    private fun sendPostRequest(request: Request): RawRequestResult {

        val reqParam = JSONObject(request.bodyAsMap()).toString()
        logger.debug(this, "Body: $reqParam")

        val mURL = URL(request.url)

        with(mURL.openConnection() as HttpsURLConnection) {
            request.headers.keys.forEach {
                setRequestProperty(it, request.headers[it])
            }
            doOutput = true
            val wr = OutputStreamWriter(outputStream)
            wr.write(reqParam)
            wr.flush()

            logger.debug(this, "URL : $url")
            logger.debug(this, "Response Code : $responseCode")

            if (responseCode == 200) {
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    logger.debug(this, "Response : $response")
                    return RawRequestResult(
                        RequestResultType.SUCCESS,
                        response.toString().toByteArray()
                    )
                }
            } else return RawRequestResult(
                RequestResultType.ERROR,
                "Error: code is $responseCode".toByteArray()
            )
        }
    }

    interface Logger {
        fun debug(obj: Any, message: String?)
        fun debug(obj: Any, message: JSONObject)
        fun error(obj: Any, message: String?)
        fun error(obj: Any, exception: Exception)
    }
}