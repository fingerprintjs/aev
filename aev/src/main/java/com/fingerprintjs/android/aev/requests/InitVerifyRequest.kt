package com.fingerprintjs.android.aev.requests


import com.fingerprintjs.android.aev.signals.SignalProvider
import com.fingerprintjs.android.aev.transport.Request
import com.fingerprintjs.android.aev.transport.RequestResultType
import com.fingerprintjs.android.aev.transport.RequestType
import com.fingerprintjs.android.aev.transport.TypedRequestResult
import org.json.JSONObject


internal data class InitVerifyResponse(
    val requestId: String,
    val errorMessage: String? = ""
)

internal class InitVerifyResult(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<InitVerifyResponse>(type, rawResponse) {
    override fun typedResult(): InitVerifyResponse {
        val errorResponse = InitVerifyResponse("", rawResponse?.toString(Charsets.UTF_8))
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return errorResponse
        return try {
            val jsonBody = JSONObject(body)
            val requestId = jsonBody.getString(REQUEST_ID_KEY)
            InitVerifyResponse(requestId)
        } catch (exception: Exception) {
            errorResponse
        }
    }
}


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