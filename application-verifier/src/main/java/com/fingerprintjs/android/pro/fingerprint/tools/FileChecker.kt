package com.fingerprintjs.android.pro.fingerprint.tools

import java.io.File


data class FileInfo(
    val path: String,
    val exists: Boolean,
    val permissions: String? = null
)

interface FileChecker {
    fun extractFileInfo(path: String): FileInfo
    fun exists(path: String): Boolean
}

class FileCheckerImpl : FileChecker {
    override fun extractFileInfo(path: String): FileInfo {
        val file = File(path)
        return FileInfo(path, file.exists())
    }

    override fun exists(path: String): Boolean {
        return File(path).exists()
    }

}