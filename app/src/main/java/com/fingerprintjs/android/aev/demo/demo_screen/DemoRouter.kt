package com.fingerprintjs.android.aev.demo.demo_screen


interface DemoRouter {
    fun saveTextToBuffer(text: String)
    fun refresh()
    fun openLink(link: String)
    fun showLogs(logs: List<String>)
}
