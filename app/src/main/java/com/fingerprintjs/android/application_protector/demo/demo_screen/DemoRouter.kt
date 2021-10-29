package com.fingerprintjs.android.application_protector.demo.demo_screen


interface DemoRouter {
    fun saveTextToBuffer(text: String)
    fun refresh()
    fun openLink(link: String)
    fun showLogs(logs: List<String>)
}
