package com.fingerprintjs.android.pro.playgroundpro.signals_screen


import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.pro.playgroundpro.R


class LogAdapter(
    private val dataset: List<String>
) : RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    inner class ViewHolder(var mTextView: TextView) : RecyclerView.ViewHolder(
        mTextView
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context)
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
