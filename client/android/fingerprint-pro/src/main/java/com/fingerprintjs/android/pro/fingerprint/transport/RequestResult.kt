package com.fingerprintjs.android.pro.fingerprint.transport


enum class RequestResultType {
    SUCCESS,
    ERROR,
    TOKEN_NOT_FOUND
}

open class RequestResult(
        val type: RequestResultType,
        val rawResponse: ByteArray
)