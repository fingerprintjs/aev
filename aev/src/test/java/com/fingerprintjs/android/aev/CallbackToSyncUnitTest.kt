package com.fingerprintjs.android.aev

import com.fingerprintjs.android.aev.utils.concurrency.callbackToSync
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.Executors


class CallbackToSyncUnitTest {

    private val executor = Executors.newSingleThreadExecutor()
    private class TestException : Exception()

    @Test
    fun callbackToSyncNormalFlowTest() {
        val v = callbackToSync<Boolean?> {
            callbackApiReturningTrue {
                emit(it)
            }
        }

        assertTrue(v == true)
    }

    @Test
    fun callbackToSyncExceptionFlowTest() {
        val v = runCatching {
            callbackToSync<Boolean?> {
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
