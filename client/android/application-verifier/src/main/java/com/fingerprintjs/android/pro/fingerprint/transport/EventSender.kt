package com.fingerprintjs.android.pro.fingerprint.transport


import org.json.JSONObject
import java.io.File
import java.util.concurrent.Executors


interface EventSender {
    fun send(request: Request, listener: (RequestResult) -> (Unit))
}

class EventSenderImpl(
        private val httpClient: HttpClient,
        private val endpointURL: String,
        private val internalFileDir: String
) : EventSender {
    val executor = Executors.newSingleThreadExecutor()

    override fun send(request: Request, listener: (RequestResult) -> Unit) {
        val requestBody = JSONObject(request.bodyAsMap()).toString()

        File("$internalFileDir/request1.json").writeText(requestBody)

        executor.execute {
            val result = httpClient.performRequest(
                    request.type,
                    "$endpointURL${request.path}",
                    request.headers,
                    requestBody
            )

            listener.invoke(RequestResult(RequestResultType.SUCCESS, result))
        }
    }
}