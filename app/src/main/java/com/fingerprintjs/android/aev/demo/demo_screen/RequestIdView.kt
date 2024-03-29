package com.fingerprintjs.android.aev.demo.demo_screen


import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.aev.demo.R
import com.fingerprintjs.android.aev.demo.extensions.hide
import com.fingerprintjs.android.aev.demo.extensions.show


interface RequestIdView {
    fun setOnGetResultsButtonClickedListener(listener: () -> (Unit))
    fun setOnLogsButtonClickedListener(listener: () -> (Unit))
    fun setOnAboutRequestIdBtnClickedListener(listener: () -> (Unit))
    fun setRequestId(requestId: String)
    fun setGetResultsBtnEnabled(enabled: Boolean = true)
    fun showRequestIdProgressBar()
    fun hideRequestIdProgressBar()
}

class RequestIdViewImpl(
    private val activity: Activity
) : RequestIdView {

    private val logsButton = activity.findViewById<TextView>(R.id.logs_btn)
    private val requestIdAboutBtn = activity.findViewById<ImageView>(R.id.request_id_about_btn)
    private val getResultsButton = activity.findViewById<FrameLayout>(R.id.get_results_btn)
    private val requestIdTextView = activity.findViewById<TextView>(R.id.request_id_tv)
    private val requestIdContainer = activity.findViewById<View>(R.id.request_id_container)
    private val progressBar = activity.findViewById<ProgressBar>(R.id.progress_indicator)

    private val buttonEnabledDrawable =
        ContextCompat.getDrawable(activity, R.drawable.bg_primary_btn)
    private val buttonDisabledDrawable =
        ContextCompat.getDrawable(activity, R.drawable.bg_primary_btn_disabled)

    override fun setOnGetResultsButtonClickedListener(listener: () -> Unit) {
        activity.runOnUiThread {
            getResultsButton.setOnClickListener {
                listener.invoke()
            }
        }
    }

    override fun setOnLogsButtonClickedListener(listener: () -> Unit) {
        activity.runOnUiThread {
            logsButton.setOnClickListener {
                listener.invoke()
            }
        }
    }

    override fun setOnAboutRequestIdBtnClickedListener(listener: () -> Unit) {
        activity.runOnUiThread {
            requestIdAboutBtn.setOnClickListener {
                listener.invoke()
            }
        }
    }

    override fun setRequestId(requestId: String) {
        activity.runOnUiThread {
            hideRequestIdProgressBar()
            requestIdTextView.text = requestId
        }
    }

    override fun setGetResultsBtnEnabled(enabled: Boolean) {
        activity.runOnUiThread {
            val tv = getResultsButton.findViewById<View>(R.id.get_result_btn_tv)
            if (enabled) {
                tv.background = buttonEnabledDrawable
                getResultsButton.isClickable = true
            } else {
                tv.background = buttonDisabledDrawable
                getResultsButton.isClickable = false
            }
        }
    }

    override fun showRequestIdProgressBar() {
        activity.runOnUiThread {
            requestIdContainer.hide()
            progressBar.show()
        }
    }

    override fun hideRequestIdProgressBar() {
        activity.runOnUiThread {
            progressBar.hide()
            requestIdContainer.show()
        }
    }
}