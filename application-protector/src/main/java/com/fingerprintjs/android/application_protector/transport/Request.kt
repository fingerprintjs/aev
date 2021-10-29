package com.fingerprintjs.android.application_protector.transport


enum class RequestType {
    GET,
    POST
}

interface Request {
    val url: String
    val type: RequestType
    val headers: Map<String, String>
    fun bodyAsMap(): Map<String, Any>
}