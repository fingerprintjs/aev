package com.fingerprintjs.android.application_protector.demo.demo_screen.api


import android.content.Context
import com.fingerprintjs.android.application_protector.ApplicationProtector
import com.fingerprintjs.android.application_protector.ApplicationProtectorFactory
import com.fingerprintjs.android.application_protector.logger.Logger


class ApplicationVerifierBuilder(
    private val applicationContext: Context
) {

    private var loggers = emptyList<Logger>()
    private var url: String = ""
    private var token: String = ""

    fun withLoggers(loggers: List<Logger>): ApplicationVerifierBuilder {
        this.loggers = loggers
        return this
    }

    fun withUrl(url: String): ApplicationVerifierBuilder {
        this.url = url
        return this
    }

    fun withAuthToken(token: String): ApplicationVerifierBuilder {
        this.token = token
        return this
    }

    fun build(): ApplicationProtector {
        return ApplicationProtectorFactory.getInstance(applicationContext, url, token, loggers)
    }
}
