package com.fingerprintjs.android.pro.playgroundpro.demo


import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.pro.playgroundpro.R
import com.fingerprintjs.android.pro.playgroundpro.adapters.ResultAdapter
import com.fingerprintjs.android.pro.playgroundpro.demo.api.Verdict
import com.fingerprintjs.android.pro.playgroundpro.extensions.hide
import com.fingerprintjs.android.pro.playgroundpro.extensions.show


interface ResultsView {
    fun setOnTryAgainButtonClickedListener(listener: () -> (Unit))
    fun setOnRawResultsButtonClickedListener(listener: () -> (Unit))
    fun setOnAboutResultsBtnClickedListener(listener: () -> (Unit))
    fun setDeviceId(requestId: String)
    fun setVerdict(verdict: List<Verdict>)
    fun showResultsProgressBar()
    fun hideResultsProgressBar()
}

class ResultsViewImpl(private val activity: DemoActivity) : ResultsView {
    private val tryAgainButton: TextView = activity.findViewById(R.id.try_again_btn)
    private val rawResultsButton: TextView = activity.findViewById(R.id.raw_results_btn)
    private val aboutBtn: ImageView = activity.findViewById(R.id.about_results_btn)

    private val deviceIdTextView: TextView = activity.findViewById(R.id.device_id_tv)
    private val resultsRecyclerView: RecyclerView = activity.findViewById(R.id.results_recycler)

    private val progressIndicator: ProgressBar =
        activity.findViewById(R.id.results_progress_indicator)
    private val deviceIdContainer: View = activity.findViewById(R.id.device_id_container)

    init {
        resultsRecyclerView.layoutManager = LinearLayoutManager(activity)
        resultsRecyclerView.adapter = ResultAdapter(activity)
    }

    override fun setOnTryAgainButtonClickedListener(listener: () -> Unit) {
        tryAgainButton.setOnClickListener {
            listener.invoke()
        }
    }

    override fun setOnRawResultsButtonClickedListener(listener: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun setOnAboutResultsBtnClickedListener(listener: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun setDeviceId(requestId: String) {
        activity.runOnUiThread {
            deviceIdTextView.text = requestId
        }
    }

    override fun setVerdict(verdict: List<Verdict>) {
        (resultsRecyclerView.adapter as? ResultAdapter)?.setDataset(verdict)
    }

    override fun showResultsProgressBar() {
        activity.runOnUiThread {
            deviceIdContainer.hide()
            progressIndicator.show()
        }
    }

    override fun hideResultsProgressBar() {
        activity.runOnUiThread {
            progressIndicator.hide()
            deviceIdContainer.show()
        }
    }

}