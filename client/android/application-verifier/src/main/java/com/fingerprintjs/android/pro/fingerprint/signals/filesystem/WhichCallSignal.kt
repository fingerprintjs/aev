package com.fingerprintjs.android.pro.fingerprint.signals.filesystem


import com.fingerprintjs.android.fingerprint.signal_providers.Signal


class WhichCallSignal(
    whichCallResult: String
) : Signal<String>(
    WHICH_CALL_NAME,
    whichCallResult
) {
    override fun toMap(): Map<String, Any> {
        return mapOf(
            WHICH_CALL_NAME to value
        )
    }
}

private const val WHICH_CALL_NAME = "whichCallResult"