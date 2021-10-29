package com.fingerprintjs.android.application_protector


interface ApplicationProtector {
    fun getRequestId(listener: (String) -> (Unit))
    fun getRequestId(listener: (String) -> (Unit), errorListener: (String) -> (Unit))
}