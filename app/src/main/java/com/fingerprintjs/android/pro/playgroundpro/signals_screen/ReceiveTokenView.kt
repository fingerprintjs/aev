package com.fingerprintjs.android.pro.playgroundpro.signals_screen

import android.app.Activity
import android.os.Handler
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences
import com.fingerprintjs.android.pro.playgroundpro.LogAdapter
import com.fingerprintjs.android.pro.playgroundpro.R


interface ReceiveTokenView {
    fun setOnRunButtonClickedListener(listener: (String) -> (Unit))
    fun setOnCopyButtonClickedListener(listener: () -> Unit)
    fun setLogsDataset(dataset: List<String>)
    fun update()
}

class ReceiveTokenViewImpl(
    private val activity: Activity,
    private val preferences: ApplicationPreferences
) : ReceiveTokenView {

    private val runButton = activity.findViewById<TextView>(R.id.run_btn)
    private val copyButton = activity.findViewById<ImageView>(R.id.copy_token_button)
    private val endpointUrlInput = activity.findViewById<EditText>(R.id.endpoint_input)
    private var logsRecycler = activity.findViewById<RecyclerView>(R.id.logs_recycler)

    private var runButtonListener: (String) -> Unit = {}
    private var copyButtonListener: () -> Unit = {}

    init {
        logsRecycler.layoutManager = LinearLayoutManager(activity)
        logsRecycler.adapter = LogAdapter(emptyList())
        runButton.setOnClickListener {
            runButtonListener.invoke(endpointUrlInput.text.toString())
        }
        copyButton.setOnClickListener {
            copyButtonListener.invoke()
        }
        endpointUrlInput.setText(preferences.getEndpointUrl())
    }

    override fun setOnRunButtonClickedListener(listener: (String) -> Unit) {
        this.runButtonListener = listener
    }

    override fun setOnCopyButtonClickedListener(listener: () -> Unit) {
        this.copyButtonListener = listener
    }

    override fun setLogsDataset(dataset: List<String>) {
        logsRecycler.adapter = LogAdapter(dataset)
    }

    override fun update() {
        logsRecycler.adapter?.let {
            Handler(activity.mainLooper).post {
                it.notifyItemInserted(it.itemCount)
            }
        }
    }

}