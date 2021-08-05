package com.fingerprintjs.android.pro.fingerprint.transport.ssl


interface SSLConnectionInspector {
    fun inspectConnection(uri: String): Boolean
}

class SSLConnectionInspectorImpl(): SSLConnectionInspector {
    override fun inspectConnection(uri: String): Boolean {
        return true
    }
}
