package com.fingerprintjs.android.pro.playgroundpro.demo


import com.fingerprintjs.android.pro.fingerprint.ApplicationVerifier
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences
import com.fingerprintjs.android.pro.playgroundpro.demo.api.GetResultsInteractor
import com.fingerprintjs.android.pro.playgroundpro.demo.api.VerificationResult
import com.fingerprintjs.android.pro.playgroundpro.demo.api.ApplicationVerifierBuilder
import org.json.JSONObject
import java.util.LinkedList
import java.util.concurrent.Executors


interface ReceiveTokenPresenter {
    fun attachView(view: DemoView)
    fun detachView()
    fun attachRouter(router: DemoRouter)
}


class ReceiveTokenPresenterImpl(
    private val applicationVerifierBuilder: ApplicationVerifierBuilder,
    private val getResultsInteractor: GetResultsInteractor,
    private val preferences: ApplicationPreferences
) : ReceiveTokenPresenter {

    private val logs = LinkedList<String>()
    private var view: DemoView? = null
    private var router: DemoRouter? = null
    private var applicationVerifier: ApplicationVerifier? = null

    private val executor = Executors.newSingleThreadExecutor()

    private var requestId: String = ""

    val logger = object : Logger {
        override fun debug(obj: Any, message: String?) {
            message?.let {
                logs.add(it)
            }
        }

        override fun debug(obj: Any, message: JSONObject) {
            logs.add(message.toString(2))
        }

        override fun error(obj: Any, message: String?) {
            message?.let {
                logs.add(it)
            }
        }

        override fun error(obj: Any, exception: Exception) {
            logs.add(exception.localizedMessage ?: "")
        }
    }

    override fun attachView(view: DemoView) {
        this.view = view
        subscribeToView()
        startGettingRequestId()
    }

    override fun detachView() {
        view = null
    }

    private fun subscribeToView() {
        this.view?.apply {
            setOnGetResultsButtonClickedListener {
                setRunBtnEnabled(false)
                showResults()
                showResultsProgressBar()
                getResultsByRequestId(requestId) {
                    hideResultsProgressBar()
                    setDeviceId(it.deviceId)
                    setVerdict(it.verdicts)
                }
            }
        }
    }

    private fun startGettingRequestId() {
        applicationVerifier = applicationVerifierBuilder
            .withLoggers(listOf(logger))
            .withUrl(preferences.getEndpointUrl())
            .withAuthToken(preferences.getApiToken())
            .build()

        this.view?.apply {
            hideResults()
            showRequestIdProgressBar()
            applicationVerifier?.getToken {
                hideRequestIdProgressBar()
                requestId = it.requestId
                setRequestId(it.requestId)
            }
        }
    }
    override fun attachRouter(router: DemoRouter) {
        this.router = router
    }

    private fun getResultsByRequestId(requestId: String, listener: (VerificationResult) -> (Unit)) {
        executor.execute {
            listener.invoke(getResultsInteractor.results(requestId))
        }
    }
}