package com.fingerprintjs.android.pro.fingerprint.transport.jwt

import org.json.JSONObject

interface JwtClient {
    // Returns signed JSON according to RFC 7515
    fun signJsonBody(requestBody: Map<String, Any>): String
}

class JwtClientImpl : JwtClient {
    override fun signJsonBody(requestBody: Map<String, Any>): String {
        return JSONObject(requestBody).toString()
    }
}