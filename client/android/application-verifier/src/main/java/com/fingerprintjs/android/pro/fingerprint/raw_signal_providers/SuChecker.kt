package com.fingerprintjs.android.pro.fingerprint.raw_signal_providers


import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.tools.FileChecker
import java.io.BufferedReader
import java.io.InputStreamReader


interface SuChecker {
    fun foundSuBinaries(): List<String>
    fun resultOfWhich(): String
}

class SuCheckerImpl(
    private val fileChecker: FileChecker,
    private val logger: Logger
) : SuChecker {
    override fun foundSuBinaries() = suBinaryPaths.filter {
        fileChecker.exists(it)
    }

    override fun resultOfWhich(): String {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf(BINARY_WHICH, BINARY_SU))
            val input = BufferedReader(InputStreamReader(process.inputStream))
            input.readLine()
        } catch (t: Throwable) {
            logger.error(this, t.message.toString())
            ""
        } finally {
            process?.destroy()
        }
    }

    private val suBinaryPaths = listOf(
        "/data/local/",
        "/data/local/bin/",
        "/data/local/xbin/",
        "/sbin/",
        "/su/bin/",
        "/system/bin/",
        "/system/bin/.ext/",
        "/system/bin/failsafe/",
        "/system/sd/xbin/",
        "/system/usr/we-need-root/",
        "/system/xbin/",
        "/cache/",
        "/data/",
        "/dev/"
    )
}

private const val BINARY_SU = "su"
private const val BINARY_WHICH = "which"