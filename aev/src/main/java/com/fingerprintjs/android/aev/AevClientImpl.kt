package com.fingerprintjs.android.aev


import com.fingerprintjs.android.aev.config.Config
import com.fingerprintjs.android.aev.config.ConfigProvider
import com.fingerprintjs.android.aev.errors.UnknownInternalError
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.signals.SignalProviderImpl
import com.fingerprintjs.android.aev.utils.concurrency.callbackToSync
import com.fingerprintjs.android.aev.utils.concurrency.runInAnotherThread
import com.fingerprintjs.android.aev.utils.concurrency.runInParallel
import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.mapError


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
                    logger.debug(this, "Start getting deviceId.")
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
                binding<Triple<DeviceIdResult, FingerprintResult, Config>, Throwable> {
                    val deviceIdResult = it.first.bind()
                    val fingerprintResult = it.second.bind()
                    val config = it.third.bind()
                    Triple(deviceIdResult, fingerprintResult, config)
                }
                    .mapError(::UnknownInternalError)
                    .flatMap { (deviceIdResult, fingerprintResult, config) ->
                        apiInteractor.getRequestId(
                            signalProviderBuilder
                                .withDeviceIdResult(deviceIdResult)
                                .withFingerprintResult(fingerprintResult)
                                .withConfig(config)
                                .build()
                        )
                    }
                    .fold(
                        success = { response ->
                            logger.debug(this, "Got requestId: ${response.requestId}")
                            listener.invoke(response.requestId)
                        },
                        failure = { error ->
                            logger.debug(this, error.description)
                            listener.invoke(error.description)
                        }
                    )
            }
        }
    }
}