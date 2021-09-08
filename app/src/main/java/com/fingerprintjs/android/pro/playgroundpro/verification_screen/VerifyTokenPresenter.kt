package com.fingerprintjs.android.pro.playgroundpro.verification_screen


import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences
import java.util.LinkedList
import java.util.concurrent.Executors


interface VerifyTokenPresenter {
    fun attachView(verifyTokenView: VerifyTokenView)
    fun detachView()
}

class VerifyTokenPresenterImpl(
    private val interactor: VerifyTokenInteractor,
    private val preferences: ApplicationPreferences
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
            setSecurityToken(preferences.getLastSecurityToken())
            setOnRunButtonClickedListener {
                verifyToken(it) {
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