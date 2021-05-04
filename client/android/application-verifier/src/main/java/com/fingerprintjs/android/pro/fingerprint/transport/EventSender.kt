package com.fingerprintjs.android.pro.fingerprint.transport


import org.json.JSONObject
import java.util.concurrent.Executors


interface EventSender {
    fun send(request: Request, listener: (RequestResult) -> (Unit))
}

class EventSenderImpl(
        private val httpClient: HttpClient,
        private val endpointURL: String
) : EventSender {
    val executor = Executors.newSingleThreadExecutor()

    override fun send(request: Request, listener: (RequestResult) -> Unit) {
        val requestBody = JSONObject(request.bodyAsMap()).toString().toByteArray()

        executor.execute {
            httpClient.performRequest(
                    request.type,
                    "$endpointURL${request.path}",
                    request.headers,
                    requestBody
            ) { listener.invoke(it) }

        }
    }
}