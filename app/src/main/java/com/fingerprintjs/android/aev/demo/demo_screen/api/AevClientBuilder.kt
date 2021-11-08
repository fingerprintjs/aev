package com.fingerprintjs.android.aev.demo.demo_screen.api


import android.content.Context
import com.fingerprintjs.android.aev.AevClient
import com.fingerprintjs.android.aev.AevClientFactory
import com.fingerprintjs.android.aev.logger.Logger


class AevClientBuilder(
    private val applicationContext: Context
) {

    private var loggers = emptyList<Logger>()
    private var url: String = ""
    private var token: String = ""

    fun withLoggers(loggers: List<Logger>): AevClientBuilder {
        this.loggers = loggers
        return this
    }

    fun withUrl(url: String): AevClientBuilder {
        this.url = url
        return this
    }

    fun withAuthToken(token: String): AevClientBuilder {
        this.token = token
        return this
    }

    fun build(): AevClient {
        return AevClientFactory.getInstance(applicationContext, token, url, loggers)
    }
}
