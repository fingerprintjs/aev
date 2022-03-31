package com.fingerprintjs.android.aev.logger


import com.fingerprintjs.android.aev.BuildConfig
import org.json.JSONObject
import java.lang.Exception


internal class ConsoleLogger : Logger {
    override fun debug(obj: Any, message: String?) {
        if (BuildConfig.DEBUG) {
            message?.let {
                println("$PREFIX${obj.javaClass.canonicalName}: $it\n")
            }
        }
    }

    override fun debug(obj: Any, message: JSONObject) {
        debug(obj, message.toString(2))
    }

    override fun error(obj: Any, message: String?) {
        message?.let {
            println("$PREFIX${obj.javaClass.canonicalName}: $it\n")
        }
    }

    override fun error(obj: Any, exception: Exception) {
        error(obj, exception.message)
    }

}

private const val PREFIX = "FingerprintJS |"