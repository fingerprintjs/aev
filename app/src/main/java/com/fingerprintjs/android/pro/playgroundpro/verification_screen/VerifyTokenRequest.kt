package com.fingerprintjs.android.pro.playgroundpro.verification_screen


import com.fingerprintjs.android.pro.fingerprint.transport.Request
import com.fingerprintjs.android.pro.fingerprint.transport.RequestResultType
import com.fingerprintjs.android.pro.fingerprint.transport.TypedRequestResult
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


class Verdict(
    val description: String
)

class VerificationResult(
    val requestId: String,
    val deviceId: String,
    val verdicts: List<Verdict>
)

class VerifyTokenResponse(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<VerificationResult>(type, rawResponse) {
    override fun typedResult(): VerificationResult? {
        val errorResponse = VerificationResult("", "", emptyList())
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return errorResponse
        return try {
            val jsonBody = JSONObject(body)
            val results = jsonBody.getJSONObject(RESULTS_KEY)
            val verdictList = LinkedList<Verdict>()
            results.keys().forEach {
                verdictList.add(Verdict("$it: ${results.getJSONObject(it).toString(2)}"))
            }
            VerificationResult("", "", verdictList)
        } catch (exception: Exception) {
            errorResponse
        }
    }
}

class VerifyTokenRequest(
    endpointUrl: String,
    autorizationToken: String,
    private val securityToken: String
) : Request {
    override val url = "$endpointUrl/api/v1/results"
    override val type = "POST"
    override val headers = mapOf(
        "Content-Type" to "application/json",
        "X-Auth-Token" to autorizationToken
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()
        resultMap["requestId"] = securityToken
        return resultMap
    }
}

private const val REQUEST_ID_KEY = "requestId"
private const val DEVICE_ID_KEY = "deviceId"
private const val RESULTS_KEY = "results"
