package com.fingerprintjs.android.pro.fingerprint.signals

interface SignalProvider {
    fun signals(): List<Signal>
}

class SignalProviderImpl : SignalProvider {
    override fun signals(): List<Signal> {
        return emptyList()
    }
}