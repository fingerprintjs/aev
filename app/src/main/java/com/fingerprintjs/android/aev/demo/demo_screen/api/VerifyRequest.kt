package com.fingerprintjs.android.aev.demo.demo_screen.api


import com.fingerprintjs.android.aev.transport.Request
import com.fingerprintjs.android.aev.transport.RequestResultType
import com.fingerprintjs.android.aev.transport.RequestType
import com.fingerprintjs.android.aev.transport.TypedRequestResult
import org.json.JSONObject
import java.util.*


class Verdict(
    val name: String,
    val value: Boolean
)

class VerificationResult(
    val deviceId: String,
    val verdicts: List<Verdict>
)

class VerificationResultResponse(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<VerificationResult>(type, rawResponse) {
    override fun typedResult(): VerificationResult? {
        val errorResponse = VerificationResult("", emptyList())
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return errorResponse
        return try {
            val jsonBody = JSONObject(body)
            val deviceId = jsonBody.getString(DEVICE_ID_KEY)
            val results = jsonBody.getJSONObject(RESULTS_KEY)
            val verdictList = LinkedList<Verdict>()
            results.keys().forEach {
                val value = results.getBoolean(it)
                verdictList.add(Verdict(splitCamelCaseString(it), value))
            }
            VerificationResult(deviceId, verdictList)
        } catch (exception: Exception) {
            errorResponse
        }
    }

    private fun splitCamelCaseString(camelCaseString: String): String {
        return camelCaseString.map {
            if (it.isUpperCase()) {
                " ${it.toLowerCase()}"
            } else it
        }.joinToString("").capitalize()
    }
}

class VerifyRequest(
    endpointUrl: String,
    private val autorizationToken: String,
    private val requestId: String
) : Request {
    override val url = "$endpointUrl/api/v1/verify"
    override val type = RequestType.POST
    override val headers = mapOf(
        "Content-Type" to "application/json"
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()

        resultMap[PRIVATE_API_KEY] = autorizationToken
        resultMap[REQUEST_ID_KEY] = requestId

        return resultMap
    }
}
