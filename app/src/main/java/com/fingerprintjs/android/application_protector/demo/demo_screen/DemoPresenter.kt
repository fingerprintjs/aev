package com.fingerprintjs.android.application_protector.demo.demo_screen


import com.fingerprintjs.android.application_protector.ApplicationProtector
import com.fingerprintjs.android.application_protector.demo.ApplicationPreferences
import com.fingerprintjs.android.application_protector.demo.demo_screen.api.ApplicationVerifierBuilder
import com.fingerprintjs.android.application_protector.demo.demo_screen.api.GetResultsInteractorImpl
import com.fingerprintjs.android.application_protector.demo.demo_screen.api.VerificationResult
import com.fingerprintjs.android.application_protector.logger.Logger
import org.json.JSONObject
import java.util.*
import java.util.concurrent.Executors


interface DemoPresenter {
    fun attachView(view: DemoView)
    fun detachView()

    fun attachRouter(router: DemoRouter)
    fun detachRouter()
}

class DemoPresenterImpl(
    private val applicationVerifierBuilder: ApplicationVerifierBuilder,
    private val preferences: ApplicationPreferences
) : DemoPresenter {

    private val receiveRequestIdLogs = LinkedList<String>()
    private val verificationResultsLogs = LinkedList<String>()

    private var view: DemoView? = null
    private var router: DemoRouter? = null
    private var applicationProtector: ApplicationProtector? = null

    private val executor = Executors.newSingleThreadExecutor()

    private var requestId: String = ""

    override fun attachView(view: DemoView) {
        this.view = view
        view.dismissRefresh()
        initApplicationVerifier()
        subscribeToRequestIdView()
        subscribeToResultsView()
    }

    override fun detachView() {
        view?.apply {
            hideResults()
        }
        view = null
    }

    override fun attachRouter(router: DemoRouter) {
        this.router = router
    }

    override fun detachRouter() {
        this.router = null
    }

    private fun subscribeToRequestIdView() {
        this.view?.apply {
            hideResults()
            showRequestIdProgressBar()
            applicationProtector?.getRequestId(
                listener = {
                    hideRequestIdProgressBar()
                    requestId = it
                    setRequestId(it)
                    setGetResultsBtnEnabled(true)
                }
            ) {
                hideRequestIdProgressBar()
                setGetResultsBtnEnabled(false)
                setRequestId(it)
            }

            setOnLogsButtonClickedListener {
                router?.showLogs(receiveRequestIdLogs)
            }
            setOnRefreshListener {
                router?.refresh()
            }
            setOnAboutRequestIdBtnClickedListener {
                router?.openLink(REQUEST_ID_DOCS_URL)
            }
        }
    }

    private fun subscribeToResultsView() {
        this.view?.apply {
            setOnGetResultsButtonClickedListener {
                setGetResultsBtnEnabled(false)
                showResults()
                showResultsProgressBar()
                getResultsByRequestId(requestId) {
                    setVerdict(it.verdicts)
                    setDeviceId(it.deviceId)
                    hideResultsProgressBar()
                }
            }
            setOnRawResultsButtonClickedListener {
                router?.showLogs(verificationResultsLogs)
            }
            setOnTryAgainButtonClickedListener {
                router?.refresh()
            }
            setOnAboutResultsBtnClickedListener {
                router?.openLink(RESPONSE_DOCS_URL)
            }
        }
    }

    private fun initApplicationVerifier() {
        applicationProtector = applicationVerifierBuilder
            .withLoggers(listOf(object : Logger {
                override fun debug(obj: Any, message: String?) {
                    message?.let {
                        receiveRequestIdLogs.add(it)
                    }
                }

                override fun debug(obj: Any, message: JSONObject) {
                    receiveRequestIdLogs.add(message.toString(2))
                }

                override fun error(obj: Any, message: String?) {
                    message?.let {
                        receiveRequestIdLogs.add(it)
                    }
                }

                override fun error(obj: Any, exception: Exception) {
                    receiveRequestIdLogs.add(exception.localizedMessage ?: "")
                }
            }))
            .withUrl(preferences.getEndpointUrl())
            .withAuthToken(preferences.getApiToken())
            .build()
    }

    private fun getResultsByRequestId(
        requestId: String,
        listener: (VerificationResult) -> (Unit)
    ) {
        executor.execute {
            val getResultsInteractor = GetResultsInteractorImpl(preferences, object : Logger {
                override fun debug(obj: Any, message: String?) {
                    message?.let {
                        verificationResultsLogs.add(it)
                    }
                }

                override fun debug(obj: Any, message: JSONObject) {
                    verificationResultsLogs.add(message.toString(2))
                }

                override fun error(obj: Any, message: String?) {
                    message?.let {
                        verificationResultsLogs.add(it)
                    }
                }

                override fun error(obj: Any, exception: Exception) {
                    verificationResultsLogs.add(exception.localizedMessage ?: "")
                }
            })
            listener.invoke(getResultsInteractor.results(requestId))
        }
    }
}
