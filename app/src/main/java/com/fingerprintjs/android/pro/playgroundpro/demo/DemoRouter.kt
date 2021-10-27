package com.fingerprintjs.android.pro.playgroundpro.demo


interface DemoRouter {
    fun saveTextToBuffer(text: String)
    fun refresh()
    fun openSettings()
    fun openRepositoryLink()
    fun showLogs(logs: List<String>)
}
