package com.fingerprintjs.android.aev


import com.fingerprintjs.android.aev.config.ConfigProvider
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.signals.SignalProviderImpl
import com.fingerprintjs.android.aev.utils.concurrency.callbackToSync
import com.fingerprintjs.android.aev.utils.concurrency.runInAnotherThread
import com.fingerprintjs.android.aev.utils.concurrency.runInParallel
import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.Fingerprinter


internal class AevClientImpl(
    private val ossAgent: Fingerprinter,
    private val apiInteractor: ApiInteractor,
    private val signalProviderBuilder: SignalProviderImpl.SignalProviderBuilder,
    private val logger: Logger,
    private val configProvider: ConfigProvider,
) : AevClient {

    override fun getRequestId(listener: (String) -> Unit) {
        getRequestId(listener, {})
    }

    override fun getRequestId(listener: (String) -> Unit, errorListener: (String) -> Unit) {
        runInAnotherThread {
            runInParallel(
                {
                    logger.debug(this, "Start getting requestId.")
                    callbackToSync<DeviceIdResult> { ossAgent.getDeviceId { emit(it) } }.also {
                        logger.debug(this, "Got deviceId: ${it.deviceId}")
                    }
                },
                {
                    logger.debug(this, "Start getting fingerprint.")
                    callbackToSync<FingerprintResult> { ossAgent.getFingerprint { emit(it) } }.also {
                        logger.debug(this, "Got fingerprint: ${it.fingerprint}")
                    }
                },
                {
                    logger.debug(this, "Start getting config.")
                    configProvider.getConfig().also {
                        logger.debug(this, "Got config")
                    }
                }
            ).let {
                val deviceIdResult = it.first.getOrNull()
                val fingerprintResult = it.second.getOrNull()
                val config = it.third.getOrNull()
                if (deviceIdResult != null && fingerprintResult != null && config != null) {
                    apiInteractor.getToken(
                        signalProviderBuilder
                            .withDeviceIdResult(deviceIdResult)
                            .withFingerprintResult(fingerprintResult)
                            .withConfig(config)
                            .build()
                    ).let { response ->
                        if (response.requestId.isEmpty()) {
                            val errorMessage =
                                if (response.errorMessage.isNullOrEmpty()) "Unknown error. Check the API token or the endpoint URL and try again." else response.errorMessage

                            logger.debug(this, "The requestId hasn't been received. $errorMessage")
                            errorListener.invoke(errorMessage)
                        } else {
                            logger.debug(this, "Got requestId: ${response.requestId}")
                            listener.invoke(response.requestId)
                        }
                    }
                }
            }
        }
    }
}