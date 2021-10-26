package com.fingerprintjs.android.pro.playgroundpro.demo


import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferencesImpl
import com.fingerprintjs.android.pro.playgroundpro.R


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
        presenter =
            ReceiveTokenPresenterImpl(
                ApplicationVerifierBuilder(this.applicationContext),
                ApplicationPreferencesImpl(this)
            )
    }

    override fun saveTextToBuffer(text: String) {
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(this, "Token is copied!", Toast.LENGTH_LONG).show()
    }
}