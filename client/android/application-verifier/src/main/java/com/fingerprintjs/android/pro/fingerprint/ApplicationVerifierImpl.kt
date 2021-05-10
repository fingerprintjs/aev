package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


internal class ApplicationVerifierImpl(
        private val ossAgent: Fingerprinter,
        private val apiInteractor: ApiInteractor,
        private val logger: Logger
) : ApplicationVerifier {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun getToken(listener: (FetchTokenResponse) -> Unit) {
        executor.execute {
            ossAgent.getDeviceId { deviceIdResult ->
                ossAgent.getFingerprint { fingerprintResult ->
                    apiInteractor.getToken(deviceIdResult, fingerprintResult) {
                        listener.invoke(it)
                    }
                }
            }
        }
    }
}