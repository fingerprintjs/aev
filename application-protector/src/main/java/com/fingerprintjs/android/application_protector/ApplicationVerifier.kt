package com.fingerprintjs.android.application_protector


import com.fingerprintjs.android.application_protector.requests.FetchTokenResponse


interface ApplicationVerifier {
    fun getRequestId(listener: (FetchTokenResponse) -> (Unit))
    fun getRequestId(listener: (FetchTokenResponse) -> (Unit), errorListener: (String) -> (Unit))
}