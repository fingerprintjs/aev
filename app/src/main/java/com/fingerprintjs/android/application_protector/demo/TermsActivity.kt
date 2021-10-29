package com.fingerprintjs.android.application_protector.demo


import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.text.Charsets.UTF_8


class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val termsTv = findViewById<TextView>(R.id.terms)
        val termsHtml = extractTermsHtml()
        termsTv.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(termsHtml, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(termsHtml)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Terms of use"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun extractTermsHtml(): String {
        val sb = StringBuilder()
        val inputStream: InputStream = assets.open("terms.html")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream, UTF_8))
        var str: String?
        while (bufferedReader.readLine().also { str = it } != null) {
            sb.append(str)
        }
        bufferedReader.close()
        return sb.toString()
    }
}