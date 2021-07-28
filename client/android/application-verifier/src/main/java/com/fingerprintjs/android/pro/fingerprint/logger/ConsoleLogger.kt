package com.fingerprintjs.android.pro.fingerprint.logger


import com.fingerprintjs.android.pro.fingerprint.BuildConfig
import org.json.JSONObject
import java.lang.Exception


class ConsoleLogger : Logger {
    override fun debug(obj: Any, message: String?) {
        if (BuildConfig.DEBUG) {
            print("$PREFIX${obj.javaClass.canonicalName}: ${message ?: ""}")
            println()
        }
    }

    override fun debug(obj: Any, message: JSONObject) {
        if (BuildConfig.DEBUG) {
            print("$PREFIX${obj.javaClass.canonicalName}: ${message.toString()}")
            println()
        }
    }

    override fun error(obj: Any, message: String?) {
        print("$PREFIX${obj.javaClass.canonicalName}: ${message ?: ""}")
        println()
    }

    override fun error(obj: Any, exception: Exception) {
        print("$PREFIX${obj.javaClass.canonicalName}: ${exception.message ?: ""}")
        println()
    }

}

private const val PREFIX = "FingerprintJS |"