package com.fingerprintjs.android.pro.playgroundpro.verification_screen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fingerprintjs.android.pro.fingerprint.logger.ConsoleLogger
import com.fingerprintjs.android.pro.fingerprint.transport.OkHttpClientImpl
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferencesImpl
import com.fingerprintjs.android.pro.playgroundpro.R


class VerifyTokenFragment : Fragment() {

    private lateinit var presenter: VerifyTokenPresenter
    private lateinit var preferences: ApplicationPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.apply {
            runOnUiThread {
                preferences = ApplicationPreferencesImpl(this)
                init()
                presenter.attachView(VerifyTokenViewImpl(this, preferences))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    private fun init() {
        this.presenter = VerifyTokenPresenterImpl(
            VerifyTokenInteractorImpl(
                OkHttpClientImpl(ConsoleLogger()),
                preferences.getEndpointUrl(),
                preferences.getApiToken(),
            ),
            preferences
        )
    }
}