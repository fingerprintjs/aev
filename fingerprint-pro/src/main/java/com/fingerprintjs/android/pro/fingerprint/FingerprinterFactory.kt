package com.fingerprintjs.android.pro.fingerprint


import android.content.Context
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher


object FingerprinterFactory {

    private var configuration: Configuration = Configuration(version = 1)
    private var ossInstance: Fingerprinter? = null
    private var hasher: Hasher = MurMur3x64x128Hasher()

    @JvmStatic
    fun getInstance(
            context: Context,
            configuration: Configuration
    ): Fingerprinter {
        // 1. Initialize oss with its fingerprinter factory
        // 2. With having initialized oss instance initialize proInstance and return it.
        // 3. Make sure you implemented double-check singleton logic
    }
}