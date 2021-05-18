package com.fingerprintjs.android.pro.fingerprint.transport.ssl


interface SSLConnectionInspector {
    fun inspectConnection(uri: String): Boolean
}