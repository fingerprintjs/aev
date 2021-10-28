package com.fingerprintjs.android.pro.playgroundpro.demo


import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fingerprintjs.android.pro.playgroundpro.R
import com.fingerprintjs.android.pro.playgroundpro.extensions.hide
import com.fingerprintjs.android.pro.playgroundpro.extensions.show


interface DemoView : RequestIdView, ResultsView {
    fun hideResults()
    fun showResults()
    fun setOnRefreshListener(listener: () -> Unit)
    fun dismissRefresh()
}


class DemoViewImpl(
    private val activity: DemoActivity
) : DemoView, RequestIdView by RequestIdViewImpl(activity),
    ResultsView by ResultsViewImpl(activity) {

    private val resultsContainer: View = activity.findViewById(R.id.results_container)

    private val swipeToRefreshLayout: SwipeRefreshLayout =
        activity.findViewById(R.id.swipe_to_refresh_view)

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

    override fun setOnRefreshListener(listener: () -> Unit) {
        swipeToRefreshLayout.setOnRefreshListener {
            listener.invoke()
        }
    }

    override fun dismissRefresh() {
        activity.runOnUiThread {
            swipeToRefreshLayout.isRefreshing = false
        }
    }
}