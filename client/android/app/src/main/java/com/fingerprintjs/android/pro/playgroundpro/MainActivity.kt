package com.fingerprintjs.android.pro.playgroundpro


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.pro.fingerprint.ApplicationVerifierFactory


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val resultView = findViewById<TextView>(R.id.result)
        ApplicationVerifierFactory.getInstance(
                applicationContext,
        ).getToken {
            runOnUiThread {
                resultView.text = "DeviceId: ${it.deviceId} \n Token: ${it.token}"
            }
        }
    }
}