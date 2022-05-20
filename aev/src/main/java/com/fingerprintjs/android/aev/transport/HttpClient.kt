package com.fingerprintjs.android.aev.transport

import com.fingerprintjs.android.aev.errors.*
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.utils.result.flattenMappingError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import org.json.JSONObject
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


internal interface HttpClient {
    fun performRequest(
        request: Request
    ): Result<RawResponse, HttpClientError>
}

internal class NativeHttpClient(
    private val logger: Logger
) : HttpClient {

    // TODO: add logic for GET request
    override fun performRequest(request: Request): Result<RawResponse, HttpClientError> {
        return runCatching {
            val reqParam = JSONObject(request.bodyAsMap()).toString()
            logger.debug(this, "Body: $reqParam")

            val mURL = URL(request.url)

            with(mURL.openConnection() as HttpsURLConnection) {
                request.headers.keys.forEach {
                    setRequestProperty(it, request.headers[it])
                }
                doOutput = true
                connect()
                runCatching {
                    val wr = OutputStreamWriter(outputStream)
                    wr.write(reqParam)
                    wr.flush()

                    logger.debug(this, "URL : $url")
                    logger.debug(this, "Response Code : $responseCode")

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val body = inputStream.readBytes().toString(Charsets.UTF_8)
                        logger.debug(this, "Response : $body")
                        Ok(
                            RawResponse(body)
                        )
                    } else {
                        val body = (errorStream ?: inputStream).readBytes().toString(Charsets.UTF_8)
                        logger.debug(this, "Response failed with")
                        Err(
                            UnknownApiError(
                                responseCode,
                                body
                            )
                        )
                    }
                }
                    .flattenMappingError {
                        when (it) {
                            is IOException -> UnknownNetworkError(it)
                            else -> UnknownInternalError(it)
                        }
                    }
                    .also { disconnect() }
            }
        }.flattenMappingError {
            when (it) {
                is IOException -> UnknownNetworkError(it)
                is SocketTimeoutException -> TimeoutError
                else -> UnknownInternalError(it)
            }
        }
    }
}