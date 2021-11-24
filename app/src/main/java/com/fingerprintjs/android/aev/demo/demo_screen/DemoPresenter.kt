package com.fingerprintjs.android.aev.demo.demo_screen


import com.fingerprintjs.android.aev.AevClient
import com.fingerprintjs.android.aev.demo.ApplicationPreferences
import com.fingerprintjs.android.aev.demo.demo_screen.api.AevClientBuilder
import com.fingerprintjs.android.aev.demo.demo_screen.api.VerifyInteractorImpl
import com.fingerprintjs.android.aev.demo.demo_screen.api.VerificationResult
import com.fingerprintjs.android.aev.logger.Logger
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
    private val aevClientBuilder: AevClientBuilder,
    private val preferences: ApplicationPreferences
) : DemoPresenter {

    private val receiveRequestIdLogs = LinkedList<String>()
    private val verificationResultsLogs = LinkedList<String>()

    private var view: DemoView? = null
    private var router: DemoRouter? = null
    private var aevClient: AevClient? = null

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
            aevClient?.getRequestId(
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
                router?.openLink(CLIENT_API_DOCS_URL)
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
                router?.openLink(SERVER_API_DOCS_URL)
            }
        }
    }

    private fun initApplicationVerifier() {
        aevClient = aevClientBuilder
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
            .withPublicApiKey(preferences.getPublicApKey())
            .build()
    }

    private fun getResultsByRequestId(
        requestId: String,
        listener: (VerificationResult) -> (Unit)
    ) {
        executor.execute {
            val getResultsInteractor = VerifyInteractorImpl(preferences, object : Logger {
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
            listener.invoke(getResultsInteractor.verify(requestId))
        }
    }
}
