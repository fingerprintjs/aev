package com.fingerprintjs.android.pro.fingerprint.logger

interface Logger {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String)
}