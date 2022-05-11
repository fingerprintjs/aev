package com.fingerprintjs.android.aev

import com.fingerprintjs.android.aev.utils.concurrency.fromCallback
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.Executors


internal class FromCallbackUnitTest {

    private val executor = Executors.newSingleThreadExecutor()
    private class TestException : Exception()

    @Test
    fun fromCallbackNormalFlowTest() {
        val v = fromCallback<Boolean?> {
            callbackApiReturningTrue {
                emit(it)
            }
        }

        assertTrue(v == true)
    }

    @Test
    fun fromCallbackExceptionFlowTest() {
        val v = runCatching {
            fromCallback<Boolean?> {
                callbackApiThrowingException {
                    emit(it)
                }
            }
        }

        assertTrue(v.exceptionOrNull() is TestException)
    }

    private fun callbackApiReturningTrue(callback: (Boolean?) -> Unit) {
        executor.execute {
            Thread.sleep(500)
            callback.invoke(true)
        }
    }

    private fun callbackApiThrowingException(
        @Suppress("UNUSED_PARAMETER")
        callback: (Boolean?) -> Unit
    ) {
        throw TestException()
    }
}
