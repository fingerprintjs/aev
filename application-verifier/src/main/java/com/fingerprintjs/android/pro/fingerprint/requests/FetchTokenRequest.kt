package com.fingerprintjs.android.pro.fingerprint.requests


import com.fingerprintjs.android.pro.fingerprint.signals.SignalProvider
import com.fingerprintjs.android.pro.fingerprint.transport.Request
import com.fingerprintjs.android.pro.fingerprint.transport.RequestResultType
import com.fingerprintjs.android.pro.fingerprint.transport.RequestType
import com.fingerprintjs.android.pro.fingerprint.transport.TypedRequestResult
import org.json.JSONObject


data class FetchTokenResponse(
    val token: String,
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