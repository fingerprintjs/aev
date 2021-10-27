package com.fingerprintjs.android.pro.playgroundpro.demo


import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferencesImpl
import com.fingerprintjs.android.pro.playgroundpro.R
import com.fingerprintjs.android.pro.playgroundpro.demo.api.ApplicationVerifierBuilder
import com.fingerprintjs.android.pro.playgroundpro.dialogs.LogsDialogView


class DemoActivity : AppCompatActivity(), DemoRouter {

    private lateinit var presenter: ReceiveTokenPresenter

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
        val applicationPreferences = ApplicationPreferencesImpl(this)
        presenter =
            ReceiveTokenPresenterImpl(
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

    override fun openSettings() {
        TODO("Not yet implemented")
    }

    override fun openLink() {
        TODO("Not yet implemented")
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
