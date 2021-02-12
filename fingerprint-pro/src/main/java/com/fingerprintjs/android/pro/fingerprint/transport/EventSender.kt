package com.fingerprintjs.android.pro.fingerprint.transport

interface EventSender {
    fun send(event: Event)
}