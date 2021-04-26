package com.fingerprintjs.android.pro.fingerprint.logger

import android.util.Log

class ConsoleLogger : Logger {
    override fun debug(tag: String, message: String) {
        if (BuildConfig.DEBUG) {

        }
    }

    override fun error(tag: String, message: String) {
        Log.e(tag, message)
    }

    private fun 

}