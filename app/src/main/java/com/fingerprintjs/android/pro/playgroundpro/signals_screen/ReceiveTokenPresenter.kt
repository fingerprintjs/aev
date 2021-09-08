package com.fingerprintjs.android.pro.playgroundpro.signals_screen


import androidx.core.graphics.rotationMatrix
import com.fingerprintjs.android.pro.fingerprint.ApplicationVerifier
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences
import org.json.JSONObject
import java.util.LinkedList


interface ReceiveTokenPresenter {
    fun attachView(view: ReceiveTokenView)
    fun detachView()
    fun attachRouter(router: ReceiveTokenRouter)
}


class ReceiveTokenPresenterImpl(
    private val applicationVerifierBuilder: ApplicationVerifierBuilder,
    private val preferences: ApplicationPreferences
) : ReceiveTokenPresenter {

    private val logs = LinkedList<String>()
    private var view: ReceiveTokenView? = null
    private var router: ReceiveTokenRouter? = null
    private var applicationVerifier: ApplicationVerifier? = null

    private var receivedToken: String = ""

    val logger = object : Logger {
        override fun debug(obj: Any, message: String?) {
            message?.let {
                logs.add(it)
                view?.update()
            }
        }

        override fun debug(obj: Any, message: JSONObject) {
            logs.add(message.toString(2))
            view?.update()
        }

        override fun error(obj: Any, message: String?) {
            message?.let {
                logs.add(it)
                view?.update()
            }
        }

        override fun error(obj: Any, exception: Exception) {
            logs.add(exception.localizedMessage ?: "")
            view?.update()
        }
    }

    override fun attachView(view: ReceiveTokenView) {
        this.view = view
        view.apply {
            setLogsDataset(logs)
            setOnRunButtonClickedListener { url ->
                applicationVerifier = applicationVerifierBuilder
                    .withLoggers(listOf(logger))
                    .withUrl(url)
                    .withAuthToken(preferences.getApiToken())
                    .build()

                applicationVerifier?.getToken {
                    saveTokenToPreferences(it.token)
                    receivedToken = it.token
                }
            }
            setOnCopyButtonClickedListener {
                router?.saveTextToBuffer(receivedToken)
            }
        }
    }

    override fun detachView() {
        view = null
    }

    override fun attachRouter(router: ReceiveTokenRouter) {
        this.router = router
    }

    private fun saveTokenToPreferences(token: String) {
        preferences.setLastSecurityToken(token)
    }

}