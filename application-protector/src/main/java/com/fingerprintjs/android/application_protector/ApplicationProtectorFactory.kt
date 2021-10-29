package com.fingerprintjs.android.application_protector


import android.content.Context
import android.hardware.SensorManager
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher
import com.fingerprintjs.android.application_protector.logger.ConsoleLogger
import com.fingerprintjs.android.application_protector.logger.Logger
import com.fingerprintjs.android.application_protector.raw_signal_providers.PackageManagerInfoProviderImpl
import com.fingerprintjs.android.application_protector.raw_signal_providers.SensorsDataCollector
import com.fingerprintjs.android.application_protector.raw_signal_providers.SensorsDataCollectorImpl
import com.fingerprintjs.android.application_protector.signals.SignalProviderImpl
import com.fingerprintjs.android.application_protector.transport.OkHttpClientImpl
import org.json.JSONObject


object ApplicationProtectorFactory {

    private var ossInstance: Fingerprinter? = null
    private var hasher: Hasher = MurMur3x64x128Hasher()
    private var ossConfiguration: Configuration = Configuration(version = 1, hasher)
    private val logger = getLogger()

    private var instance: ApplicationProtector? = null

    private var consoleLogger = ConsoleLogger()
    private var outerLoggers: List<Logger>? = null


    @JvmStatic
    fun getInstance(
        applicationContext: Context,
        endpointUrl: String,
        authToken: String,
        outerLoggers: List<Logger> = emptyList()
    ): ApplicationProtector {
        val ossInstance = FingerprinterFactory.getInstance(applicationContext, ossConfiguration)
        ApplicationProtectorFactory.ossInstance = ossInstance
        ApplicationProtectorFactory.outerLoggers = outerLoggers

        val instance = ApplicationProtectorImpl(
            ossInstance,
            getApiInteractor(
                endpointUrl,
                getAppName(applicationContext),
                authToken
            ),
            getSignalProviderBuilder(applicationContext),
            getLogger()
        )

        ApplicationProtectorFactory.instance = instance
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
        authToken
    )

    private fun getHttpClient() = OkHttpClientImpl(logger)

    private fun getSignalProviderBuilder(context: Context) =
        SignalProviderImpl.SignalProviderBuilder(
            PackageManagerInfoProviderImpl(context.packageManager),
            getSensorsDataCollector(context)
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
