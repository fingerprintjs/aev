package com.fingerprintjs.android.aev.demo.utils.network

interface Request {
    val url: String
    val type: RequestType
    val headers: Map<String, String>
    fun bodyAsMap(): Map<String, Any>
}

enum class RequestType {
    GET,
    POST
}

open class RawRequestResult(
    val type: RequestResultType,
    val rawResponse: ByteArray?
)

enum class RequestResultType {
    SUCCESS,
    ERROR
}

abstract class TypedRequestResult<T>(
    type: RequestResultType,
    rawResponse: ByteArray?
) : RawRequestResult(type, rawResponse) {
    abstract fun typedResult(): T?
}
