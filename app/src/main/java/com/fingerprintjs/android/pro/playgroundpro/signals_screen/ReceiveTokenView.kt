package com.fingerprintjs.android.pro.playgroundpro.signals_screen


import android.os.Handler
import android.view.View
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
    view: View,
    preferences: ApplicationPreferences
) : ReceiveTokenView {

    private val runButton = view.findViewById<TextView>(R.id.run_btn)
    private val copyButton = view.findViewById<ImageView>(R.id.copy_token_button)
    private val endpointUrlInput = view.findViewById<EditText>(R.id.endpoint_input)
    private var logsRecycler = view.findViewById<RecyclerView>(R.id.receive_logs_recycler)

    private val adapter = LogAdapter(view.context)

    private var runButtonListener: (String) -> Unit = {}
    private var copyButtonListener: () -> Unit = {}

    init {
        logsRecycler.layoutManager = LinearLayoutManager(view.context)
        logsRecycler.adapter = adapter
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
        (logsRecycler.adapter as? LogAdapter)?.setDataset(dataset)
    }

    override fun update() {
        logsRecycler.adapter?.let {
            Handler(logsRecycler.context.mainLooper).post {
                it.notifyItemInserted(it.itemCount)
            }
        }
    }

}