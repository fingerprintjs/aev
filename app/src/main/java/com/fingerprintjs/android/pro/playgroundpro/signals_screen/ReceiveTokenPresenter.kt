package com.fingerprintjs.android.pro.playgroundpro.signals_screen


import com.fingerprintjs.android.pro.fingerprint.ApplicationVerifier
import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.playgroundpro.ApplicationPreferences
import org.json.JSONObject
import java.util.LinkedList


interface ReceiveTokenPresenter {
    fun attachView(view: ReceiveTokenView)
    fun detachView()
}


class ReceiveTokenPresenterImpl(
    private val applicationVerifierBuilder: ApplicationVerifierBuilder
) : ReceiveTokenPresenter {

    private val logs = LinkedList<String>()
    private var view: ReceiveTokenView? = null
    private var applicationVerifier: ApplicationVerifier? = null

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
                    .build()

                applicationVerifier?.getToken {
                    handleToken(it.token)
                }
                handleToken("Button clicked")
            }
        }
    }

    override fun detachView() {
        view = null
    }

    private fun handleToken(token: String) {
        print(token)
    }

}