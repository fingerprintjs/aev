package com.fingerprintjs.android.pro.fingerprint.signals.filesystem


import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.pro.fingerprint.raw_signal_providers.MountedPathInfo


class MountedPathsSignal(
    mountedFiles: List<MountedPathInfo>
) : Signal<List<MountedPathInfo>>(
    MOUNTED_PATHS_NAME,
    mountedFiles
) {
    override fun toMap(): Map<String, Any> {
        return mapOf(MOUNTED_PATHS_NAME to value.map {
            mapOf(
                MOUNTED_PATHS_KEY to it.path,
                MOUNTED_PATHS_OPTIONS_KEY to it.options
            )
        })
    }
}

private const val MOUNTED_PATHS_NAME = "mountedPaths"
private const val MOUNTED_PATHS_KEY = "path"
private const val MOUNTED_PATHS_OPTIONS_KEY = "options"