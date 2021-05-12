package com.fingerprintjs.android.pro.fingerprint.logger


import com.fingerprintjs.android.pro.fingerprint.BuildConfig


class ConsoleLogger : Logger {
    override fun debug(obj: Any, message: String) {
        if (BuildConfig.DEBUG) {
            print("$PREFIX${obj.javaClass.canonicalName}: $message")
            println()
        }
    }

    override fun error(obj: Any, message: String) {
        print("$PREFIX${obj.javaClass.canonicalName}: $message")
        println()
    }
}

private const val PREFIX = "FingerprintJS |"