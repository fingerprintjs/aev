package com.fingerprintjs.android.application_protector.demo.extensions


import android.view.View


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
