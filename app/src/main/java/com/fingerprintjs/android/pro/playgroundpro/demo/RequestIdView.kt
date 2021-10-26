package com.fingerprintjs.android.pro.playgroundpro.demo


import android.app.Activity
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
    private val progressBar = activity.findViewById<ProgressBar>(R.id.progress_indicator)

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
        // Change background dependently on the flag
    }

    override fun showRequestIdProgressBar() {
        activity.runOnUiThread {
            requestIdTextView.hide()
            progressBar.show()
        }
    }

    override fun hideRequestIdProgressBar() {
        activity.runOnUiThread {
            progressBar.hide()
            requestIdTextView.show()
        }
    }
}