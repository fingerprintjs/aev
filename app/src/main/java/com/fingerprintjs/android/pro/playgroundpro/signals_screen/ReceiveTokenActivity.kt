package com.fingerprintjs.android.pro.playgroundpro.signals_screen


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.pro.playgroundpro.R


class ReceiveTokenActivity : AppCompatActivity() {

    private lateinit var presenter: ReceiveTokenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        init()
        presenter.attachView(ReceiveTokenViewImpl(this))
    }

    private fun init() {
        presenter = ReceiveTokenPresenterImpl(ApplicationVerifierBuilder(applicationContext))
    }
}