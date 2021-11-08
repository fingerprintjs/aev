package com.fingerprintjs.android.aev.transport


interface HttpClient {
    fun performRequest(
        request: Request
    ): RawRequestResult
}