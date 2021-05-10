package com.fingerprintjs.android.pro.fingerprint.logger


import com.fingerprintjs.android.pro.fingerprint.BuildConfig


class ConsoleLogger : Logger {
    override fun debug(obj: Any, message: String) {
        if (BuildConfig.DEBUG) {
            print("${obj.javaClass.canonicalName}: $message")
        }
    }

    override fun error(obj: Any, message: String) {
        print("${obj.javaClass.canonicalName}: $message")
    }
}