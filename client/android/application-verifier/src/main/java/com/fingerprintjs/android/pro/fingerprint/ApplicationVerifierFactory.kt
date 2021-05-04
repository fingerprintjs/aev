package com.fingerprintjs.android.pro.fingerprint


import android.content.Context
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher
import com.fingerprintjs.android.pro.fingerprint.transport.EventSenderImpl
import com.fingerprintjs.android.pro.fingerprint.transport.OkHttpClientImpl


object ApplicationVerifierFactory {

    private var ossInstance: Fingerprinter? = null
    private var hasher: Hasher = MurMur3x64x128Hasher()
    private var ossConfiguration: Configuration = Configuration(version = 1, hasher)

    private var instance: ApplicationVerifier? = null

    @JvmStatic
    fun getInstance(
            context: Context,
            apiToken: String,
            endpointUrl: String? = null
    ): ApplicationVerifier {
        val ossInstance = FingerprinterFactory.getInstance(context, ossConfiguration)
        this.ossInstance = ossInstance


        val instance = ApplicationVerifierImpl(
                ossInstance,
                getCoreApiInteractor(
                        endpointUrl ?: DEFAULT_ENDPOINT_URL,
                        apiToken,
                        getAppName(context),
                        context
                )
        )

        this.instance = instance
        return instance
    }

    private fun getCoreApiInteractor(
            endpointUrl: String,
            apiToken: String,
            appName: String,
            context: Context
    ) = ApiInteractorImpl(
            getEventSender(
                    endpointUrl
            ),
            apiToken,
            appName
    )

    private fun getEventSender(
            endpointUrl: String
    ) = EventSenderImpl(
            getHttpClient(),
            endpointUrl
    )

    private fun getHttpClient() = OkHttpClientImpl()

    private fun getAppName(context: Context) = context.applicationInfo.packageName.toString()
}


private const val DEFAULT_ENDPOINT_URL = BuildConfig.BACKEND_URL