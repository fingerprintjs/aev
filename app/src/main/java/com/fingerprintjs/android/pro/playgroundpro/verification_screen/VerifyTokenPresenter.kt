package com.fingerprintjs.android.pro.playgroundpro.verification_screen

import java.util.*
import java.util.concurrent.Executors


interface VerifyTokenPresenter {
    fun attachView(verifyTokenView: VerifyTokenView)
    fun detachView()
}

class VerifyTokenPresenterImpl(
    private val interactor: VerifyTokenInteractor
) : VerifyTokenPresenter {

    private var view: VerifyTokenView? = null
    private val executor = Executors.newSingleThreadExecutor()
    private val logs = LinkedList<String>()

    override fun attachView(verifyTokenView: VerifyTokenView) {
        this.view = verifyTokenView
        subscribeToView()
    }

    override fun detachView() {
        this.view = null
    }

    private fun subscribeToView() {
        this.view?.apply {
            setLogsDataset(logs)
            setOnRunButtonClickedListener {
                verifyToken(it) {
                    // Update view, store response to logs
                    logs.add("deviceId: ${it.deviceId}")
                    logs.add("requestId: ${it.requestId}")
                    logs.add("verdict:\n")
                    it.verdicts.forEach {
                        logs.add(it.description)
                    }
                    update()
                }
            }
        }
    }

    private fun verifyToken(token: String, listener: (VerificationResult) -> (Unit)) {
        executor.execute {
            listener.invoke(interactor.verifyToken(token))
        }
    }
}