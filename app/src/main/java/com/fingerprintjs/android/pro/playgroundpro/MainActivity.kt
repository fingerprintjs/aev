package com.fingerprintjs.android.pro.playgroundpro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.pro.fingerprint.FingerprinterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FingerprinterFactory.getInstance(
                applicationContext,
                "apiToken",
                "https://api.fpjs.io"
        ).getFingerprint {
            runOnUiThread {
                Toast.makeText(this, it.fingerprint, Toast.LENGTH_LONG).show()
            }
        }
    }
}