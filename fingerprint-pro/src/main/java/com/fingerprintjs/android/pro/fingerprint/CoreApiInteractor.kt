package com.fingerprintjs.android.pro.fingerprint

import com.fingerprintjs.android.fingerprint.signal_providers.Signal

interface CoreApiInteractor {
    fun getVisitorId(signals: List<Signal<*>>, token: String, listener: (RequestResult) -> (Unit))
}

data class RequestResult(
        val visitorId: String,
        val type: RequestResultType
)

enum class RequestResultType {
    SUCCESS,
    ERROR,
    TOKEN_NOT_FOUND
}