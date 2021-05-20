package com.fingerprintjs.android.pro.fingerprint.signals.root


import com.fingerprintjs.android.pro.fingerprint.signals.VerificationSignal
import org.json.JSONObject


data class RootData(
    val adbEnabled: Boolean,
    val foundBinaryPaths: List<String>,
    val foundRWPaths: List<String>,
    val environmentProperties: List<String>,
    val mountedPaths: List<String>
)

class RootSignal(data: RootData) : VerificationSignal<RootData>(
    ROOT_DATA_NAME, ROOT_DATA_DISPLAY_NAME, data
) {
    override fun toMap(): Map<String, Any> {
        TODO("Not yet implemented")
    }

    override fun toString() = JSONObject(toMap()).toString(2)
}

private const val ROOT_DATA_DISPLAY_NAME = "Root detection data"
private const val ROOT_DATA_NAME = "rootDetectionData"

