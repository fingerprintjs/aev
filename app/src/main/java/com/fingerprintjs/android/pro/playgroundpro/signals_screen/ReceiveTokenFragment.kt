package com.fingerprintjs.android.pro.playgroundpro.signals_screen


import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferencesImpl
import com.fingerprintjs.android.pro.playgroundpro.R


class ReceiveTokenFragment : Fragment(), ReceiveTokenRouter {

    private lateinit var presenter: ReceiveTokenPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        presenter.attachRouter(this)
        presenter.attachView(
            ReceiveTokenViewImpl(
                view,
                ApplicationPreferencesImpl(requireContext())
            )
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_receive, container, false)
        return view
    }

    private fun init() {
        presenter =
            ReceiveTokenPresenterImpl(
                ApplicationVerifierBuilder(requireContext().applicationContext),
                ApplicationPreferencesImpl(requireContext())
            )
    }

    override fun saveTextToBuffer(text: String) {
        val clipboard: ClipboardManager? =
            getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText("", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Token is copied!", Toast.LENGTH_LONG).show()
    }
}
