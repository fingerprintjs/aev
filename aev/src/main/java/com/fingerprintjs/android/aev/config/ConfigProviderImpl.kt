package com.fingerprintjs.android.aev.config

internal class ConfigProviderImpl(

): ConfigProvider {

    override fun getConfig(): Config {
        return Config.DEFAULT
    }
}
