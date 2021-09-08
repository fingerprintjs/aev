package com.fingerprintjs.android.pro.playgroundpro


import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class LogAdapter(
    private val context: Context
) : RecyclerView.Adapter<LogAdapter.ViewHolder>() {
    private var dataset: List<String> = emptyList()

    inner class ViewHolder(var mTextView: TextView) : RecyclerView.ViewHolder(
        mTextView
    )

    fun setDataset(dataset: List<String>) {
        this.dataset = dataset
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(context)
            .inflate(R.layout.log_msg, parent, false) as TextView

        view.typeface = Typeface.MONOSPACE
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.text = dataset[position]
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}
