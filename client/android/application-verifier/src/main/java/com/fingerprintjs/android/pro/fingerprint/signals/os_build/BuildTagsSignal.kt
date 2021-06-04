package com.fingerprintjs.android.pro.fingerprint.signals.os_build


import android.os.Build
import com.fingerprintjs.android.fingerprint.signal_providers.Signal


class BuildTagsSignal(
    buildTags: List<String> = Build.TAGS.split(",")
) : Signal<List<String>>(
    BUILD_TAGS_NAME,
    buildTags
) {
    override fun toMap() = mapOf(
        BUILD_TAGS_NAME to value
    )
}

private const val BUILD_TAGS_NAME = "buildTags"