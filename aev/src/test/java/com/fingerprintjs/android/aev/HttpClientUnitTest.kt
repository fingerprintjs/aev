package com.fingerprintjs.android.aev

import com.cloned.github.michaelbull.result.get
import com.cloned.github.michaelbull.result.getError
import com.fingerprintjs.android.aev.errors.UnknownApiError
import com.fingerprintjs.android.aev.errors.UnknownNetworkError
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.transport.HttpClientImpl
import com.fingerprintjs.android.aev.transport.Request
import com.fingerprintjs.android.aev.transport.RequestType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.internal.TlsUtil
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class HttpClientUnitTest {

    private val webServer = MockWebServer()
    private val localHostSslSocketFactory = TlsUtil.localhost().sslSocketFactory()
    private val client = HttpClientImpl.create(
        logger = getDummyLogger(),
        sslPinningConfig = HttpClientImpl.SSLPinningConfig(
            pinnedCerts = emptyList()
        ),
        sslSocketFactory = localHostSslSocketFactory
    )

    @Before
    fun setUp() {
        webServer.start()
        webServer.useHttps(localHostSslSocketFactory, false)
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun responseWithOkCode() {
        val url = webServer.url("/")
        webServer.enqueue(MockResponse().setBody("hello, world!").setResponseCode(200))

        val request = getDummyRequest(url.toString())

        val response = client.performRequest(request)

        assertTrue(response.get()?.body == "hello, world!")
    }

    @Test
    fun responseWithSuccessfulCode() {
        val url = webServer.url("/")
        webServer.enqueue(MockResponse().setBody("hello, world!").setResponseCode(201))

        val request = getDummyRequest(url.toString())

        val response = client.performRequest(request)

        val error = response.getError()
        assertTrue(error is UnknownApiError)
        error as UnknownApiError
        assertTrue(error.errorCode == 201)
        assertTrue(error.body == "hello, world!")
    }

    @Test
    fun responseWithErrorCode() {
        val url = webServer.url("/")
        webServer.enqueue(MockResponse().setBody("hello, world!").setResponseCode(401))

        val request = getDummyRequest(url.toString())

        val response = client.performRequest(request)

        val error = response.getError()
        assertTrue(error is UnknownApiError)
        error as UnknownApiError
        assertTrue(error.errorCode == 401)
        assertTrue(error.body == "hello, world!")
    }


    @Test
    fun requestToUnknownHost() {
        val url = "https://blablabla123.com/"

        val request = getDummyRequest(url)

        val response = client.performRequest(request)

        val error = response.getError()
        assertTrue(error is UnknownNetworkError)
    }


    private fun getDummyRequest(url: String): Request {
        return object : Request {
            override val url: String
                get() = url
            override val type: RequestType
                get() = RequestType.GET
            override val headers: Map<String, String>
                get() = emptyMap()

            override fun bodyAsMap(): Map<String, Any> {
                return emptyMap()
            }
        }
    }

    private fun getDummyLogger(): Logger {
        return object : Logger {
            override fun debug(obj: Any, message: String?) {
            }

            override fun debug(obj: Any, message: JSONObject) {
            }

            override fun error(obj: Any, message: String?) {
            }

            override fun error(obj: Any, exception: Exception) {
            }
        }
    }

}