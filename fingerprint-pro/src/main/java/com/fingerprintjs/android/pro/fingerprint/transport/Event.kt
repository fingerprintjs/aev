package com.fingerprintjs.android.pro.fingerprint.transport


interface Event {
    val path: String
    val type: String
    val headers: Map<String, String>
    fun asMap(): Map<String, Any>
}