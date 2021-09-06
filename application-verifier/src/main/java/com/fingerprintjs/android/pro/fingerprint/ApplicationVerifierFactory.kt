package com.fingerprintjs.android.pro.fingerprint


import android.content.Context
import android.os.Build
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher
import com.fingerprintjs.android.pro.fingerprint.logger.ConsoleLogger
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.raw_signal_providers.MountedPathsReaderImpl
import com.fingerprintjs.android.pro.fingerprint.raw_signal_providers.SuCheckerImpl
import com.fingerprintjs.android.pro.fingerprint.signals.SignalProviderImpl
import com.fingerprintjs.android.pro.fingerprint.tools.FileCheckerImpl
import com.fingerprintjs.android.pro.fingerprint.transport.OkHttpClientImpl
import com.fingerprintjs.android.pro.fingerprint.transport.jwt.JwtClient
import com.fingerprintjs.android.pro.fingerprint.transport.jwt.JwtClientImpl
import com.fingerprintjs.android.pro.fingerprint.transport.ssl.SSLConnectionInspector
import com.fingerprintjs.android.pro.fingerprint.transport.ssl.SSLConnectionInspectorImpl
import org.json.JSONObject
import java.lang.Exception


object ApplicationVerifierFactory {

    private var ossInstance: Fingerprinter? = null
    private var hasher: Hasher = MurMur3x64x128Hasher()
    private var ossConfiguration: Configuration = Configuration(version = 1, hasher)
    private val logger = getLogger()

    private var instance: ApplicationVerifier? = null

    private var consoleLogger = ConsoleLogger()
    private var outerLoggers: List<Logger>? = null


    @JvmStatic
    fun getInstance(
        context: Context,
        endpointUrl: String,
        authToken: String,
        outerLoggers: List<Logger> = emptyList()
    ): ApplicationVerifier {
        val ossInstance = FingerprinterFactory.getInstance(context, ossConfiguration)
        this.ossInstance = ossInstance
        this.outerLoggers = outerLoggers

        val instance = ApplicationVerifierImpl(
            ossInstance,
            getApiInteractor(
                endpointUrl,
                getAppName(context),
                authToken
            ),
            getSignalProviderBuilder(),
            getLogger()
        )

        this.instance = instance
        return instance
    }

    private fun getApiInteractor(
        endpointUrl: String,
        appName: String,
        authToken: String
    ) = ApiInteractorImpl(
        getHttpClient(),
        endpointUrl,
        appName,
        logger,
        getSslConnectionInspector(),
        authToken
    )

    private fun getHttpClient() = OkHttpClientImpl(logger, getJwtClient())

    private fun getSignalProviderBuilder() =
        SignalProviderImpl.SignalProviderBuilder(
            MountedPathsReaderImpl(
                Build.VERSION.SDK_INT,
                logger
            ), SuCheckerImpl(FileCheckerImpl(), logger)
        )

    private fun getAppName(context: Context) = context.applicationInfo.packageName.toString()

    private fun getLogger(): Logger {
        return object : Logger {
            override fun debug(obj: Any, message: String?) {
                consoleLogger.debug(obj, message)
                outerLoggers?.forEach {
                    it.debug(obj, message)
                }
            }

            override fun debug(obj: Any, message: JSONObject) {
                consoleLogger.debug(obj, message)
                outerLoggers?.forEach {
                    it.debug(obj, message)
                }
            }

            override fun error(obj: Any, message: String?) {
                consoleLogger.error(obj, message)
                outerLoggers?.forEach {
                    it.error(obj, message)
                }
            }

            override fun error(obj: Any, exception: Exception) {
                consoleLogger.error(obj, exception)
                outerLoggers?.forEach {
                    it.error(obj, exception)
                }
            }
        }
    }

    private fun getSslConnectionInspector(): SSLConnectionInspector = SSLConnectionInspectorImpl()

    private fun getJwtClient(): JwtClient {
        return JwtClientImpl()
    }
}
