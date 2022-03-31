package com.fingerprintjs.android.aev.transport


internal enum class RequestType {
    GET,
    POST
}

internal interface Request {
    val url: String
    val type: RequestType
    val headers: Map<String, String>
    fun bodyAsMap(): Map<String, Any>
}