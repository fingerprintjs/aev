package com.fingerprintjs.android.pro.playgroundpro.demo


import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.pro.playgroundpro.R
import com.fingerprintjs.android.pro.playgroundpro.extensions.hide
import com.fingerprintjs.android.pro.playgroundpro.extensions.show


interface RequestIdView {
    fun setOnGetResultsButtonClickedListener(listener: () -> (Unit))
    fun setOnLogsButtonClickedListener(listener: () -> (Unit))
    fun setOnAboutRequestIdBtnClickedListener(listener: () -> (Unit))
    fun setRequestId(requestId: String)
    fun setRunBtnEnabled(enabled: Boolean = true)
    fun showRequestIdProgressBar()
    fun hideRequestIdProgressBar()
}

class RequestIdViewImpl(
    private val activity: Activity
) : RequestIdView {

    private val logsButton = activity.findViewById<TextView>(R.id.logs_btn)
    private val requestIdAboutBtn = activity.findViewById<ImageView>(R.id.request_id_about_btn)
    private val getResultsButton = activity.findViewById<TextView>(R.id.get_results_btn)
    private val requestIdTextView = activity.findViewById<TextView>(R.id.request_id_tv)
    private val requestIdContainer = activity.findViewById<View>(R.id.request_id_container)
    private val progressBar = activity.findViewById<ProgressBar>(R.id.progress_indicator)

    private val buttonEnabledDrawable =  ContextCompat.getDrawable(activity, R.drawable.bg_primary_btn)
    private val buttonDisabledDrawable = ContextCompat.getDrawable(activity, R.drawable.bg_primary_btn_disabled)

    override fun setOnGetResultsButtonClickedListener(listener: () -> Unit) {
        getResultsButton.setOnClickListener {
            listener.invoke()
        }
    }

    override fun setOnLogsButtonClickedListener(listener: () -> Unit) {
        logsButton.setOnClickListener {
            listener.invoke()
        }
    }

    override fun setOnAboutRequestIdBtnClickedListener(listener: () -> Unit) {
        requestIdAboutBtn.setOnClickListener {
            listener.invoke()
        }
    }

    override fun setRequestId(requestId: String) {
        activity.runOnUiThread {
            hideRequestIdProgressBar()
            requestIdTextView.text = requestId
        }
    }

    override fun setRunBtnEnabled(enabled: Boolean) {
        if (enabled) {
            getResultsButton.background = buttonEnabledDrawable
            getResultsButton.isClickable = true
        } else {
            getResultsButton.background = buttonDisabledDrawable
            getResultsButton.isClickable = false
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