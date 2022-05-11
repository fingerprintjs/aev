package com.fingerprintjs.android.aev.config

internal class ConfigProviderImpl(

): ConfigProvider {

    override fun getConfig(): Config {
        Thread.sleep(50) // emulating file read
        return Config.DEFAULT
    }
}
