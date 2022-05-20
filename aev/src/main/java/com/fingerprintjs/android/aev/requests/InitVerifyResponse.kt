package com.fingerprintjs.android.aev.requests

import com.fingerprintjs.android.aev.errors.ParseError
import com.fingerprintjs.android.aev.transport.RawResponse
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import org.json.JSONObject

internal data class InitVerifyResponse(
    val requestId: String,
) {
    companion object {
        fun from(rawResponse: RawResponse): Result<InitVerifyResponse, ParseError> {
            val text = rawResponse.body
            return runCatching {
                val json = JSONObject(text)
                val requestId = json.getString(REQUEST_ID_KEY)
                InitVerifyResponse(requestId)
            }.mapError { ParseError(text, InitVerifyResponse::class.java) }
        }
    }
}
