package com.fingerprintjs.android.application_protector.demo.demo_screen


import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.application_protector.demo.ActionMenuActivity
import com.fingerprintjs.android.application_protector.demo.R
import com.fingerprintjs.android.application_protector.demo.demo_screen.api.ApplicationVerifierBuilder
import com.fingerprintjs.android.application_protector.demo.dialogs.LogsDialogView


class DemoActivity : ActionMenuActivity(), DemoRouter {

    private lateinit var presenter: DemoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        init()
        presenter.attachRouter(this)
        presenter.attachView(
            DemoViewImpl(
                this
            )
        )
    }

    private fun init() {
        presenter =
            DemoPresenterImpl(
                ApplicationVerifierBuilder(this.applicationContext),
                applicationPreferences
            )
    }

    override fun saveTextToBuffer(text: String) {
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(this, "Copied!", Toast.LENGTH_LONG).show()
    }

    override fun refresh() {
        presenter.detachRouter()
        presenter.detachView()
        init()
        presenter.attachRouter(this)
        presenter.attachView(
            DemoViewImpl(
                this
            )
        )
    }

    override fun openLink(link: String) {
        val webpage: Uri = Uri.parse(link)
        openLink(webpage)
    }

    override fun showLogs(logs: List<String>) {
        LogsDialogView(this).showLogs(logs) {
            val sb = StringBuilder()
            logs.forEach {
                sb.append("$it \n")
            }
            saveTextToBuffer(sb.toString())
        }
    }
}
