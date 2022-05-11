package com.fingerprintjs.android.aev.config

import com.fingerprintjs.android.aev.annotations.WorkerThread

internal interface ConfigProvider {

    @WorkerThread
    fun getConfig(): Config
}
