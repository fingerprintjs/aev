package com.fingerprintjs.android.pro.playgroundpro.verification_screen


import android.app.Activity
import android.os.Handler
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences
import com.fingerprintjs.android.pro.playgroundpro.LogAdapter
import com.fingerprintjs.android.pro.playgroundpro.R


interface VerifyTokenView {
    fun setOnRunButtonClickedListener(listener: (String) -> (Unit))
    fun setLogsDataset(dataset: List<String>)
    fun setSecurityToken(token: String)
    fun update()
}

class VerifyTokenViewImpl(
    private val activity: Activity,
    preferences: ApplicationPreferences
) : VerifyTokenView {

    private val verifyButton = activity.findViewById<TextView>(R.id.verify_btn)
    private val apiTokenInput = activity.findViewById<EditText>(R.id.api_token)
    private var logsRecycler = activity.findViewById<RecyclerView>(R.id.verify_logs_recycler)

    private val adapter = LogAdapter(activity)

    private var listener: (String) -> Unit = {}

    init {
        logsRecycler.layoutManager = LinearLayoutManager(activity)
        logsRecycler.adapter = adapter
        verifyButton.setOnClickListener {
            listener.invoke(apiTokenInput.text.toString())
        }
        apiTokenInput.setText(preferences.getApiToken())
    }

    override fun setOnRunButtonClickedListener(listener: (String) -> Unit) {
        this.listener = listener
    }

    override fun setLogsDataset(dataset: List<String>) {
        (logsRecycler.adapter as? LogAdapter)?.setDataset(dataset)
    }

    override fun setSecurityToken(token: String) {
        apiTokenInput.setText(token)
    }

    override fun update() {
        logsRecycler.adapter?.let {
            Handler(activity.mainLooper).post {
                it.notifyItemInserted(it.itemCount)
            }
        }
    }
}
