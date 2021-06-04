package com.fingerprintjs.android.pro.fingerprint.raw_signal_providers


import android.os.Build
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import java.io.IOException
import java.util.Scanner


data class MountedPathInfo(
    val path: String,
    val options: List<String>
)

interface MountedPathsReader {
    fun getMountedPaths(): List<MountedPathInfo>
}

class MountedPathsReaderImpl(
    private val apiLevel: Int,
    private val logger: Logger
) : MountedPathsReader {
    override fun getMountedPaths(): List<MountedPathInfo> {
        return mountedEntries().mapNotNull {
            it.toMountedPathInfo()
        }
    }

    private fun mountedEntries(): List<String> {
        return try {
            val inputStream = Runtime.getRuntime().exec("mount").inputStream
                ?: return emptyList()
            val propVal: String = Scanner(inputStream).useDelimiter("\\A").next()
            propVal.split("\n")
        } catch (e: IOException) {
            logger.error(
                this,
                e.localizedMessage
            )
            emptyList()
        } catch (e: NoSuchElementException) {
            emptyList()
        }
    }

    private fun String.toMountedPathInfo(): MountedPathInfo? {
        val args = split(" ")

        if (
            apiLevel <= Build.VERSION_CODES.M && args.size < 4 ||
            apiLevel > Build.VERSION_CODES.M && args.size < 6
        ) {
            return null
        }

        val mountPoint: String
        var mountOptions: String

        if (apiLevel > Build.VERSION_CODES.M) {
            mountPoint = args[2]
            mountOptions = args[5]
            mountOptions = mountOptions.replace("(", "")
            mountOptions = mountOptions.replace(")", "")
        } else {
            mountPoint = args[1]
            mountOptions = args[3]
        }

        return MountedPathInfo(
            mountPoint,
            mountOptions.split(",")
        )
    }
}