package com.fingerprintjs.android.pro.playgroundpro.verification_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fingerprintjs.android.pro.playgroundpro.R
import com.fingerprintjs.android.pro.playgroundpro.signals_screen.ReceiveTokenPresenter


class VerifyTokenFragment : Fragment() {

    private lateinit var presenter: ReceiveTokenPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    private fun init() {

    }
}