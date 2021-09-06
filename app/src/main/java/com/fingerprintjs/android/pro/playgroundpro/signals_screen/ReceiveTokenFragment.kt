package com.fingerprintjs.android.pro.playgroundpro.signals_screen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferencesImpl
import com.fingerprintjs.android.pro.playgroundpro.R


class ReceiveTokenFragment : Fragment() {

    private lateinit var presenter: ReceiveTokenPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        activity?.let {
            presenter.attachView(ReceiveTokenViewImpl(it, ApplicationPreferencesImpl(requireContext())))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receive, container, false)
    }

    private fun init() {
        presenter =
            ReceiveTokenPresenterImpl(ApplicationVerifierBuilder(requireContext().applicationContext))
    }
}