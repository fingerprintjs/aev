package com.fingerprintjs.android.aev.requests


import com.fingerprintjs.android.aev.signals.SignalProvider
import com.fingerprintjs.android.aev.transport.Request
import com.fingerprintjs.android.aev.transport.RequestType


internal class InitVerifyRequest(
    endpointUrl: String,
    private val appName: String,
    private val publicApiKey: String,
    private val signalProvider: SignalProvider
) : Request {

    override val url = "$endpointUrl/api/v1/init"
    override val type = RequestType.POST
    override val headers = mapOf(
        "Content-Type" to "application/json"
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()
        val signalsMap = HashMap<String, Any>()

        signalProvider.signals().forEach {
            signalsMap[it.name] = it.toMap()
        }
        resultMap[PUBLIC_API_KEY] = publicApiKey
        resultMap[APP_NAME_KEY] = appName
        resultMap[SIGNALS_KEY] = signalsMap

        return resultMap
    }
}