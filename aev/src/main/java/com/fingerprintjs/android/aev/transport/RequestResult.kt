package com.fingerprintjs.android.aev.transport


internal enum class RequestResultType {
    SUCCESS,
    ERROR
}

internal open class RawRequestResult(
    val type: RequestResultType,
    val rawResponse: ByteArray?
)

internal abstract class TypedRequestResult<T>(
    type: RequestResultType,
    rawResponse: ByteArray?
) : RawRequestResult(type, rawResponse) {
    abstract fun typedResult(): T?
}