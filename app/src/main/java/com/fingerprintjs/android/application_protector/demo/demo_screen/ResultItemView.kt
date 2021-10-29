package com.fingerprintjs.android.application_protector.demo.demo_screen


import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.application_protector.demo.R


interface ResultItemView {
    fun setName(name: String)
    fun setValue(value: Boolean)
}

class ResultItemViewImpl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : CardView(context, attrs, defStyle), ResultItemView {

    private val detectedString = context.getString(R.string.verdict_detected_string)
    private val notDetectedString = context.getString(R.string.verdict_not_detected_string)

    private val detectedColor = ContextCompat.getColor(context, R.color.red)
    private val notDetectedColor = ContextCompat.getColor(context, R.color.green)

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
}
