package com.fingerprintjs.android.pro.fingerprint.requests


import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.pro.fingerprint.transport.Request
import com.fingerprintjs.android.pro.fingerprint.transport.RequestResultType
import com.fingerprintjs.android.pro.fingerprint.transport.TypedRequestResult
import org.json.JSONObject


data class FetchTokenResponse(
    val token: String
)

class FetchTokenRequestResult(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<FetchTokenResponse>(type, rawResponse) {
    override fun typedResult(): FetchTokenResponse {
        val errorResponse = FetchTokenResponse("")
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
    private val signals: List<Signal<*>>
) : Request {

    override val url = "$endpointUrl/api/v1/request"
    override val type = "POST"
    override val headers = mapOf(
        "App-Name" to appName,
        "Content-Type" to "application/json"
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()
        resultMap[SIGNALS_KEY] = signals.map {
            it.toMap()
        }
        return resultMap
    }
}

private const val SIGNALS_KEY = "signals"