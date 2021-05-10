package com.fingerprintjs.android.pro.fingerprint.logger


interface Logger {
    fun debug(obj: Any, message: String)
    fun error(obj: Any, message: String)
}