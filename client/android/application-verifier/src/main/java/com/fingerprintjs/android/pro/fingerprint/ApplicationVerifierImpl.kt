package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.Fingerprinter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


internal class ApplicationVerifierImpl(
        private val ossAgent: Fingerprinter,
        private val apiInteractor: ApiInteractor
) : ApplicationVerifier {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun getToken(listener: (String) -> Unit) {
        executor.execute {
            ossAgent.getDeviceId { deviceIdResult ->
                ossAgent.getFingerprint { fingerprintResult ->
                    apiInteractor.getToken(deviceIdResult, fingerprintResult) {
                        listener.invoke(it.token)
                    }
                }
            }
        }
    }
}