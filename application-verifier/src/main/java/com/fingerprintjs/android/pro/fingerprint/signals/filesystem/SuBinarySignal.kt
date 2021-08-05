package com.fingerprintjs.android.pro.fingerprint.signals.filesystem

import com.fingerprintjs.android.fingerprint.signal_providers.Signal


class SuBinarySignal(
    foundSuBinary: List<String>
) : Signal<List<String>>(
    SU_BINARY_FOUND_NAME, foundSuBinary
) {
    override fun toMap() = mapOf(SU_BINARY_FOUND_NAME to value)
}

private const val SU_BINARY_FOUND_NAME = "foundSuBinaries"