package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


internal class FingerprinterPro(
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
                ossAgent.getFingerprint(StabilityLevel.UNIQUE) { fingerprintResult ->
                    coreApiInteractor.getVisitorId(
                            deviceId,
                            fingerprintResult
                    ) { requestResult ->
                        object : FingerprintResult {
                            override val fingerprint = requestResult.visitorId
                            override fun <T> getSignalProvider(clazz: Class<T>) = fingerprintResult.getSignalProvider(clazz)
                        }
                    }
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
}