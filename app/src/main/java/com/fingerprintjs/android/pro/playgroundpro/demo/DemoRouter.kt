package com.fingerprintjs.android.pro.playgroundpro.demo


interface DemoRouter {
    fun saveTextToBuffer(text: String)
    fun refresh()
    fun openLink(link: String)
    fun showLogs(logs: List<String>)
}
