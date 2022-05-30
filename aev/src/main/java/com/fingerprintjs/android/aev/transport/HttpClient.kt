package com.fingerprintjs.android.aev.transport

import android.util.Base64
import com.cloned.github.michaelbull.result.*
import com.fingerprintjs.android.aev.annotations.VisibleForTesting
import com.fingerprintjs.android.aev.errors.*
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.utils.result.flattenMappingError
import org.json.JSONObject
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.security.MessageDigest
import java.security.PublicKey
import java.security.cert.Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory


internal interface HttpClient {
    fun performRequest(
        request: Request
    ): Result<RawResponse, HttpClientError>
}

internal class NativeHttpClient private constructor(
    private val logger: Logger,
    private val sslPinningConfig: SSLPinningConfig,
    private val sslSocketFactory: SSLSocketFactory?
) : HttpClient {

    // TODO: add logic for GET request
    override fun performRequest(request: Request): Result<RawResponse, HttpClientError> {
        return runCatching {
            val reqParam = JSONObject(request.bodyAsMap()).toString()
            logger.debug(this, "Body: $reqParam")

            val mURL = URL(request.url)

            with(mURL.openConnection() as HttpsURLConnection) {
                this@NativeHttpClient.sslSocketFactory?.let { this.sslSocketFactory = it }
                request.headers.keys.forEach {
                    setRequestProperty(it, request.headers[it])
                }
                doOutput = true
                connect()
                runCatching afterConnected@ {
                    if (!checkCertificates(this.serverCertificates)) {
                        return@afterConnected Err(SSLPinningError)
                    }
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

    private fun checkCertificates(certificates: Array<Certificate>): Boolean = runCatching {
        sslPinningConfig.pinnedCerts.forEach { pinnedCertInfo ->
            val certForCheck = certificates[pinnedCertInfo.positionInChain]
            if (!checkPublicKey(certForCheck.publicKey, pinnedCertInfo.subjPubKeySha256Base64)) {
                return@runCatching false
            }
        }
        true
    }.getOr(false)

    private fun checkPublicKey(
        pubKey: PublicKey,
        expectedPubKeySha256Base64: String
    ): Boolean = runCatching {
        val pubKeyEncoded = pubKey.encoded
        val pubKeySha256 = MessageDigest.getInstance("SHA-256").digest(pubKeyEncoded)
        val pubKeySha256Base64 =
            Base64.encodeToString(pubKeySha256, Base64.DEFAULT or Base64.NO_WRAP)
        pubKeySha256Base64 == expectedPubKeySha256Base64
    }.getOr(false)


    data class SSLPinningConfig(
        val pinnedCerts: List<PinnedCertInfo>
    ) {
        data class PinnedCertInfo(
            val positionInChain: Int,
            val subjPubKeySha256Base64: String,
        )
    }

    companion object {

        fun create(
            logger: Logger,
            sslPinningConfig: SSLPinningConfig,
        ): NativeHttpClient = NativeHttpClient(logger, sslPinningConfig, null)

        @VisibleForTesting(otherwise = VisibleForTesting.Otherwise.NONE)
        fun create(
            logger: Logger,
            sslPinningConfig: SSLPinningConfig,
            sslSocketFactory: SSLSocketFactory?,
        ): NativeHttpClient = NativeHttpClient(logger, sslPinningConfig, sslSocketFactory)
    }
}