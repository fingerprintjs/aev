package com.fingerprintjs.android.pro.fingerprint.transport


interface HttpClient {
    fun performRequest(
        request: Request
    ): RawRequestResult
}