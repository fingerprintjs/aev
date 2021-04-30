package com.fingerprintjs.android.pro.fingerprint.logger


import com.fingerprintjs.android.pro.fingerprint.BuildConfig

class ConsoleLogger : Logger {
    override fun debug(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            print("$tag: $message")
        }
    }

    override fun error(tag: String, message: String) {
        print("$tag: $message")
    }
}