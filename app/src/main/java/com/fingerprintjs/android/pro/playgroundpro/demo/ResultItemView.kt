package com.fingerprintjs.android.pro.playgroundpro.demo


import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.fingerprintjs.android.pro.playgroundpro.R


interface ResultItemView {
    fun setName(name: String)
    fun setValue(value: Boolean)
}

class ResultItemViewImpl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : CardView(context, attrs, defStyle), ResultItemView {

    private val detectedString = context.getString(R.string.verdict_detected_string)
    private val notDetectedString = context.getString(R.string.verdict_not_detected_string)

    private val detectedColor = context.getColor(R.color.red)
    private val notDetectedColor = context.getColor(R.color.green)

    private val nameTv: TextView by lazy {
        findViewById(R.id.name)
    }
    private val valueTv: TextView by lazy {
        findViewById(R.id.value)
    }

    override fun setName(name: String) {
        nameTv.text = name
    }

    override fun setValue(value: Boolean) {
        if (value) {
            valueTv.text = detectedString
            valueTv.setTextColor(detectedColor)
        } else {
            valueTv.text = notDetectedString
            valueTv.setTextColor(notDetectedColor)
        }
    }

    private fun getStringRepresentationForValue(value: Boolean) = when(value) {
        true -> detectedString
        false -> notDetectedString
    }
}
