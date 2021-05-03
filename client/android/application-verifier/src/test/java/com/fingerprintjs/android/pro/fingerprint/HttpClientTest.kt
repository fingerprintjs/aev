package com.fingerprintjs.android.pro.fingerprint

import com.fingerprintjs.android.pro.fingerprint.transport.HttpClientImpl
import org.junit.Test

class HttpClientTest {
    @Test
    fun `google request success`() {
        val client = HttpClientImpl()

        val result = client.performRequest("GET", "https://google.com", emptyMap())

        print(result)
    }
}