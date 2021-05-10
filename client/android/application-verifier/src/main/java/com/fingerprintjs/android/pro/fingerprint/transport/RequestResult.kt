package com.fingerprintjs.android.pro.fingerprint.transport


enum class RequestResultType {
    SUCCESS,
    ERROR
}

open class RawRequestResult(
        val type: RequestResultType,
        val rawResponse: ByteArray?
)

abstract class TypedRequestResult<T>(
        type: RequestResultType,
        rawResponse: ByteArray?
) : RawRequestResult(type, rawResponse) {
    abstract fun result(): T?
}