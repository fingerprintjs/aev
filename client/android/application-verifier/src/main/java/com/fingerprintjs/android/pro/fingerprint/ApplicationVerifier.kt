package com.fingerprintjs.android.pro.fingerprint

interface ApplicationVerifier {
    fun getToken(listener: (String) -> (Unit))
}