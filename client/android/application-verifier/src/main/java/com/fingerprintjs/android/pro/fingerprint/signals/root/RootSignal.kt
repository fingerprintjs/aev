package com.fingerprintjs.android.pro.fingerprint.signals.root


import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import org.json.JSONObject


data class RootData(
    val foundBinaryPaths: List<String>,
    val foundRWPaths: List<String>,
    val environmentProperties: List<String>,
    val mountedPaths: List<String>
)

class RootSignal(data: RootData) : Signal<RootData>(
    ROOT_DATA_NAME, data
) {
    override fun toMap(): Map<String, Any> {
        //TODO Perform decomposition of signals
        return emptyMap()
    }

    override fun toString() = JSONObject(toMap()).toString(2)
}

private const val ROOT_DATA_NAME = "rootDetectionData"

