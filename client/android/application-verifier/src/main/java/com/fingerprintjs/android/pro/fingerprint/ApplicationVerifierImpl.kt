package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenResponse
import com.fingerprintjs.android.pro.fingerprint.signals.SignalProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


internal class ApplicationVerifierImpl(
        private val ossAgent: Fingerprinter,
        private val apiInteractor: ApiInteractor,
        private val signalProvider: SignalProvider,
        private val logger: Logger
) : ApplicationVerifier {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun getToken(listener: (FetchTokenResponse) -> Unit) {
        executor.execute {
            logger.debug(this, "Start getting token." )
            ossAgent.getDeviceId { deviceIdResult ->
                logger.debug(this, "Got deviceId: ${deviceIdResult.deviceId}" )
                ossAgent.getFingerprint { fingerprintResult ->
                    logger.debug(this, "Got fingerprint: ${fingerprintResult.fingerprint}")
                    apiInteractor.getToken(deviceIdResult, fingerprintResult, signalProvider)?.let {
                        logger.debug(this, "Got token: ${it.token}")
                        listener.invoke(it)
                    }
                }
            }
        }
    }
}