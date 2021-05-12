package com.fingerprintjs.android.pro.fingerprint


import android.content.Context
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher
import com.fingerprintjs.android.pro.fingerprint.logger.ConsoleLogger
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.signals.SignalProviderImpl
import com.fingerprintjs.android.pro.fingerprint.transport.OkHttpClientImpl
import com.fingerprintjs.android.pro.fingerprint.transport.RequestPerformerImpl


object ApplicationVerifierFactory {

    private var ossInstance: Fingerprinter? = null
    private var hasher: Hasher = MurMur3x64x128Hasher()
    private var ossConfiguration: Configuration = Configuration(version = 1, hasher)
    private val logger = getLogger()

    private var instance: ApplicationVerifier? = null

    @JvmStatic
    fun getInstance(
        context: Context,
        endpointUrl: String? = null
    ): ApplicationVerifier {
        val ossInstance = FingerprinterFactory.getInstance(context, ossConfiguration)
        this.ossInstance = ossInstance


        val instance = ApplicationVerifierImpl(
            ossInstance,
            getApiInteractor(
                endpointUrl ?: DEFAULT_ENDPOINT_URL,
                getAppName(context)
            ),

            getSignalProvider(),
            logger
        )

        this.instance = instance
        return instance
    }

    private fun getApiInteractor(
        endpointUrl: String,
        appName: String
    ) = ApiInteractorImpl(
        getEventSender(
            endpointUrl
        ),
        appName,
        logger
    )

    private fun getEventSender(
        endpointUrl: String
    ) = RequestPerformerImpl(
        getHttpClient(),
        endpointUrl,
        logger
    )

    private fun getHttpClient() = OkHttpClientImpl(logger)

    private fun getSignalProvider() = SignalProviderImpl()

    private fun getAppName(context: Context) = context.applicationInfo.packageName.toString()

    private fun getLogger(): Logger {
        return ConsoleLogger()
    }
}


private const val DEFAULT_ENDPOINT_URL = BuildConfig.BACKEND_URL