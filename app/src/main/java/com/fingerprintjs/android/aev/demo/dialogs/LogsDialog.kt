package com.fingerprintjs.android.aev.demo.dialogs


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.aev.demo.R
import com.fingerprintjs.android.aev.demo.adapters.LogAdapter


interface LogsDialog {
    fun showLogs(logs: List<String>, onCopyBtnListener: () -> (Unit))
    fun dismiss()
}

class LogsDialogView(private val activity: Activity) : LogsDialog {

    private var dialog: Dialog? = null

    override fun showLogs(logs: List<String>, onCopyBtnListener: () -> (Unit)) {
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(it)
        }

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_logs, null)

        val logsRecycler = view.findViewById<RecyclerView>(R.id.logs_recycler)
        val adapter = LogAdapter(view.context)

        val copyBtn = view.findViewById<ImageView>(R.id.copy_token_button)
        copyBtn.setOnClickListener {
            onCopyBtnListener.invoke()
        }

        logsRecycler.layoutManager = LinearLayoutManager(view.context)
        logsRecycler.adapter = adapter

        builder.setView(view)
        adapter.setDataset(logs)

        dialog = builder.create()
        dialog?.show()
    }

    override fun dismiss() {
        dialog?.dismiss()
    }
}