package com.fingerprintjs.android.pro.fingerprint.logger

import java.lang.Exception


interface Logger {
    fun debug(obj: Any, message: String?)
    fun error(obj: Any, message: String?)
    fun error(obj: Any, exception: Exception)
}