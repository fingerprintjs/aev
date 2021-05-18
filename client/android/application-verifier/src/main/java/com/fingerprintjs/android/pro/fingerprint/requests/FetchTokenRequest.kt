package com.fingerprintjs.android.pro.fingerprint.requests


import com.fingerprintjs.android.pro.fingerprint.signals.Signal
import com.fingerprintjs.android.pro.fingerprint.transport.Request
import com.fingerprintjs.android.pro.fingerprint.transport.RequestResultType
import com.fingerprintjs.android.pro.fingerprint.transport.TypedRequestResult
import org.json.JSONObject


data class FetchTokenResponse(
    val token: String,
    val deviceId: String
)

class FetchTokenRequestResult(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<FetchTokenResponse>(type, rawResponse) {
    override fun result(): FetchTokenResponse {
        val errorResponse = FetchTokenResponse("", "")
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return errorResponse
        return try {
            val jsonBody = JSONObject(body)
            val deviceId = jsonBody.getString(DEVICE_ID_RESPONSE_KEY)
            val token = jsonBody.getString(TOKEN_RESPONSE_KEY)
            FetchTokenResponse(token, deviceId)
        } catch (exception: Exception) {
            errorResponse
        }
    }
}


class FetchTokenRequest(
    endpointUrl: String,
    appName: String,
    private val signals: List<Signal>
) : Request {

    override val url = "$endpointUrl/verify"
    override val type = "POST"
    override val headers = mapOf(
        "App-Name" to appName,
        "Content-Type" to "application/json"
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()

        val deviceIdSignal = mapOf(
            STATE_KEY to "0",
            VALUE_KEY to mapOf(
                ANDROID_ID_KEY to deviceIdResult.androidId,
                GSF_ID_KEY to deviceIdResult.gsfId
            )
        )

        resultMap[DEVICE_ID_SECTION_KEY] = deviceIdSignal

        return resultMap
    }
}