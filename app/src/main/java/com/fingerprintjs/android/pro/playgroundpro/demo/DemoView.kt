package com.fingerprintjs.android.pro.playgroundpro.demo


import android.view.View
import com.fingerprintjs.android.pro.playgroundpro.R
import com.fingerprintjs.android.pro.playgroundpro.extensions.hide
import com.fingerprintjs.android.pro.playgroundpro.extensions.show


interface DemoView : RequestIdView, ResultsView {
    fun hideResults()
    fun showResults()
}


class DemoViewImpl(
    private val activity: DemoActivity
) : DemoView, RequestIdView by RequestIdViewImpl(activity), ResultsView by ResultsViewImpl(activity) {

    private val resultsContainer: View = activity.findViewById(R.id.results_container)

    override fun hideResults() {
        activity.runOnUiThread {
            resultsContainer.hide()
        }
    }

    override fun showResults() {
        activity.runOnUiThread {
            resultsContainer.show()
        }
    }
}