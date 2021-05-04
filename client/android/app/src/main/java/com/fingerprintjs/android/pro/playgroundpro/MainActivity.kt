package com.fingerprintjs.android.pro.playgroundpro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.pro.fingerprint.ApplicationVerifierFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ApplicationVerifierFactory.getInstance(
                applicationContext,
                "apiToken"
        ).getToken {
            runOnUiThread {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}