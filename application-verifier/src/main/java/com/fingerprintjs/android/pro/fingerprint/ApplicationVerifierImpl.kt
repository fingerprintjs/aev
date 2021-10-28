package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenResponse
import com.fingerprintjs.android.pro.fingerprint.signals.SignalProviderImpl
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


internal class ApplicationVerifierImpl(
    private val ossAgent: Fingerprinter,
    private val apiInteractor: ApiInteractor,
    private val signalProviderBuilder: SignalProviderImpl.SignalProviderBuilder,
    private val logger: Logger
) : ApplicationVerifier {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun getToken(listener: (FetchTokenResponse) -> Unit) {
        getToken(listener, {})
    }

    override fun getToken(listener: (FetchTokenResponse) -> Unit, errorListener: (String) -> Unit) {
        executor.execute {
            logger.debug(this, "Start getting token.")
            ossAgent.getDeviceId { deviceIdResult ->
                logger.debug(this, "Got deviceId: ${deviceIdResult.deviceId}")
                ossAgent.getFingerprint { fingerprintResult ->
                    logger.debug(this, "Got fingerprint: ${fingerprintResult.fingerprint}")
                    apiInteractor.getToken(
                        signalProviderBuilder
                            .withDeviceIdResult(deviceIdResult)
                            .withFingerprintResult(fingerprintResult)
                            .build()
                    ).let {
                        if (it.requestId.isEmpty()) {
                            val errorMessage =
                                if (it.errorMessage.isNullOrEmpty()) "Unknown error. Check the API token or the endpoint URL and try again." else it.errorMessage

                            logger.debug(this, "Token hasn't been received. $errorMessage")
                            errorListener.invoke(errorMessage)
                        } else {
                            logger.debug(this, "Got token: ${it.requestId}")
                            listener.invoke(it)
                        }
                    }
                }
            }
        }
    }
}