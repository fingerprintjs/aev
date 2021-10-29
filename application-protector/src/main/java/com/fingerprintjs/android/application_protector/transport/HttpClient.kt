package com.fingerprintjs.android.application_protector.transport


interface HttpClient {
    fun performRequest(
        request: Request
    ): RawRequestResult
}