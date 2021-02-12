package com.fingerprintjs.android.pro.fingerprint

import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FingerprinterPro(
        private val ossAgent: Fingerprinter,
        private val coreApiInteractor: CoreApiInteractor
) : Fingerprinter {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun getDeviceId(listener: (DeviceIdResult) -> Unit) {
        ossAgent.getDeviceId(listener)
    }

    override fun getFingerprint(listener: (FingerprintResult) -> Unit) {
        executor.execute {
            ossAgent.getDeviceId { deviceId ->
                ossAgent.getFingerprint(StabilityLevel.UNIQUE) {
                    fingerprint ->
                    coreApiInteractor.getVisitorId(

                    )
                }
            }
        }
    }

    override fun getFingerprint(stabilityLevel: StabilityLevel, listener: (FingerprintResult) -> Unit) {
        getFingerprint(listener)
    }

    override fun getFingerprint(signalProvidersMask: Int, listener: (FingerprintResult) -> Unit) {
        getFingerprint(listener)
    }

    private fun extractToken(): String {

    }

    private fun getSignals(): List<Signal<*>> {

    }
}