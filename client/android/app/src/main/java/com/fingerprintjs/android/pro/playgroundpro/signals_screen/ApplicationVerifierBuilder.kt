package com.fingerprintjs.android.pro.playgroundpro.signals_screen


import android.content.Context
import com.fingerprintjs.android.pro.fingerprint.ApplicationVerifier
import com.fingerprintjs.android.pro.fingerprint.ApplicationVerifierFactory
import com.fingerprintjs.android.pro.fingerprint.logger.Logger


class ApplicationVerifierBuilder(
    private val applicationContext: Context
) {

    private var loggers = emptyList<Logger>()
    private var url: String? = null

    fun withLoggers(loggers: List<Logger>): ApplicationVerifierBuilder {
        this.loggers = loggers
        return this
    }

    fun withUrl(url: String): ApplicationVerifierBuilder  {
        this.url = url
        return this
    }

    fun build(): ApplicationVerifier {
        return ApplicationVerifierFactory.getInstance(applicationContext, url, loggers)
    }
}
