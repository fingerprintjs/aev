package com.fingerprintjs.android.pro.fingerprint.signals


import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel


abstract class VerificationSignal<T>(
    name: String,
    displayName: String,
    value: T
) : Signal<T>(
    0,
    null,
    StabilityLevel.STABLE,
    name,
    displayName,
    value
) {

    constructor(signal: Signal<T>) : this(
        signal.name,
        signal.displayName,
        signal.value
    )

    abstract fun toMap(): Map<String, Any>
}