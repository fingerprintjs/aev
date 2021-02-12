package com.fingerprintjs.android.pro.fingerprint.transport

interface Event {
    fun asMap(): Map<String, String>
}