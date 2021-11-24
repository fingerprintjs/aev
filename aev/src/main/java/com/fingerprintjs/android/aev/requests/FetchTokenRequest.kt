package com.fingerprintjs.android.aev.requests


import com.fingerprintjs.android.aev.signals.SignalProvider
import com.fingerprintjs.android.aev.transport.Request
import com.fingerprintjs.android.aev.transport.RequestResultType
import com.fingerprintjs.android.aev.transport.RequestType
import com.fingerprintjs.android.aev.transport.TypedRequestResult
import org.json.JSONObject


data class FetchTokenResponse(
    val requestId: String,
    val errorMessage: String? = ""
)

class FetchTokenRequestResult(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<FetchTokenResponse>(type, rawResponse) {
    override fun typedResult(): FetchTokenResponse {
        val errorResponse = FetchTokenResponse("", rawResponse?.toString(Charsets.UTF_8))
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return errorResponse
        return try {
            val jsonBody = JSONObject(body)
            val token = jsonBody.getString(TOKEN_RESPONSE_KEY)
            FetchTokenResponse(token)
        } catch (exception: Exception) {
            errorResponse
        }
    }
}


class FetchTokenRequest(
    endpointUrl: String,
    appName: String,
    private val autorizationToken: String,
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
        resultMap["publicApiKey"] = autorizationToken
        resultMap["signals"] = signalsMap

        return resultMap
    }
}