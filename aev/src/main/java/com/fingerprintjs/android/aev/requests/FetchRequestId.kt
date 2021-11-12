package com.fingerprintjs.android.aev.requests


import com.fingerprintjs.android.aev.signals.SignalProvider
import com.fingerprintjs.android.aev.transport.Request
import com.fingerprintjs.android.aev.transport.RequestResultType
import com.fingerprintjs.android.aev.transport.RequestType
import com.fingerprintjs.android.aev.transport.TypedRequestResult
import org.json.JSONObject


data class FetchRequestIdResponse(
    val requestId: String,
    val errorMessage: String? = ""
)

class FetchRequestIdResult(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<FetchRequestIdResponse>(type, rawResponse) {
    override fun typedResult(): FetchRequestIdResponse {
        val errorResponse = FetchRequestIdResponse("", rawResponse?.toString(Charsets.UTF_8))
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return errorResponse
        return try {
            val jsonBody = JSONObject(body)
            val token = jsonBody.getString(REQUEST_ID_RESPONSE_KEY)
            FetchRequestIdResponse(token)
        } catch (exception: Exception) {
            errorResponse
        }
    }
}


class FetchRequestId(
    endpointUrl: String,
    appName: String,
    autorizationToken: String,
    private val signalProvider: SignalProvider
) : Request {

    override val url = "$endpointUrl/api/v1/verify"
    override val type = RequestType.POST
    override val headers = mapOf(
        "App-Name" to appName,
        "Content-Type" to "application/json",
        "Auth-Token" to autorizationToken
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()
        val signalsMap = HashMap<String, Any>()

        signalProvider.signals().forEach {
            signalsMap[it.name] = it.toMap()
        }

        resultMap["signals"] = signalsMap

        return resultMap
    }
}