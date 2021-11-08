package com.fingerprintjs.android.aev


interface AevClient {
    fun getRequestId(listener: (String) -> (Unit))
    fun getRequestId(listener: (String) -> (Unit), errorListener: (String) -> (Unit))
}