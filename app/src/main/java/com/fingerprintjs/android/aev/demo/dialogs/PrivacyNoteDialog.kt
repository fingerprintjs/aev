package com.fingerprintjs.android.aev.demo.dialogs


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.aev.demo.R


class PrivacyNoteDialog(
    private val activity: Activity,
    private val onTermsClickedListener: () -> (Unit),
    private val onAgreeClickedListener: () -> (Unit)
) {

    private var dialog: AlertDialog? = null

    private val accentColor = ContextCompat.getColor(activity, R.color.orange)

    @SuppressLint("InflateParams")
    fun show() {
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(it)
        }

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_privacy, null)

        dialog = builder
            .setTitle("Privacy note")
            .setPositiveButton(
                "Agree"
            ) { _, _ ->
                dismiss()
                onAgreeClickedListener.invoke()
            }
            .setNegativeButton(
                "Read more"
            ) { _, _ ->
                dismiss()
                onTermsClickedListener.invoke()
            }
            .setView(view)
            .setCancelable(false)

            .create()

        dialog?.show()

        val negativeButton = dialog?.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton?.setTextColor(accentColor)

        val positiveButton = dialog?.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton?.setTextColor(accentColor)
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}