package com.fingerprintjs.android.pro.fingerprint.logger

import org.json.JSONObject
import java.lang.Exception


interface Logger {
    fun debug(obj: Any, message: String?)
    fun debug(obj: Any, message: JSONObject)
    fun error(obj: Any, message: String?)
    fun error(obj: Any, exception: Exception)
}