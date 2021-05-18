package com.fingerprintjs.android.pro.fingerprint.signals


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult


interface SignalProvider {
    fun signals(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult
    ): List<Signal>
}

class SignalProviderImpl() : SignalProvider {
    override fun signals(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult
    ): List<Signal> {
        // Aggregate all signals from different sources and return them in one list
        return emptyList()
    }
}