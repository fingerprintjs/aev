package com.fingerprintjs.android.aev


import android.content.Context
import android.hardware.SensorManager
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher
import com.fingerprintjs.android.aev.logger.ConsoleLogger
import com.fingerprintjs.android.aev.logger.Logger
import com.fingerprintjs.android.aev.raw_signal_providers.PackageManagerInfoProviderImpl
import com.fingerprintjs.android.aev.raw_signal_providers.SensorsDataCollector
import com.fingerprintjs.android.aev.raw_signal_providers.SensorsDataCollectorImpl
import com.fingerprintjs.android.aev.signals.SignalProviderImpl
import com.fingerprintjs.android.aev.signals.app.AppSignalProviderImpl
import com.fingerprintjs.android.aev.signals.user_profile.UserProfileSignalProviderImpl
import com.fingerprintjs.android.aev.transport.NativeHttpClient
import org.json.JSONObject


object AevClientFactory {

    private var fingerprintJSInstance: Fingerprinter? = null
    private var ossConfiguration: Configuration = Configuration(version = 3, MurMur3x64x128Hasher())
    private val logger = getLogger()

    private var instance: AevClient? = null

    private var consoleLogger = ConsoleLogger()
    private var outerLoggers: List<Logger>? = null


    @JvmStatic
    @JvmOverloads
    fun getInstance(
        applicationContext: Context,
        publicApiKey: String,
        endpointUrl: String = DEFAULT_ENDPOINT_URL,
        outerLoggers: List<Logger> = emptyList()
    ): AevClient {
        val ossInstance = FingerprinterFactory.getInstance(applicationContext, ossConfiguration)
        fingerprintJSInstance = ossInstance
        AevClientFactory.outerLoggers = outerLoggers

        val instance = AevClientImpl(
            ossInstance,
            getApiInteractor(
                endpointUrl,
                getAppName(applicationContext),
                publicApiKey
            ),
            getSignalProviderBuilder(applicationContext),
            logger
        )

        AevClientFactory.instance = instance
        return instance
    }

    private fun getApiInteractor(
        endpointUrl: String,
        appName: String,
        publicApiKey: String
    ) = ApiInteractorImpl(
        getHttpClient(),
        endpointUrl,
        appName,
        logger,
        publicApiKey
    )

    private fun getHttpClient() = NativeHttpClient(logger)

    private fun getSignalProviderBuilder(context: Context) =
        SignalProviderImpl.SignalProviderBuilder(
            PackageManagerInfoProviderImpl(context.packageManager),
            getSensorsDataCollector(context),
            UserProfileSignalProviderImpl(context),
            AppSignalProviderImpl(context),
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

    private fun getSensorsDataCollector(context: Context): SensorsDataCollector {
        return SensorsDataCollectorImpl(context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    }
}

private const val DEFAULT_ENDPOINT_URL = "https://aev.fpapi.io"
