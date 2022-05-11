package com.fingerprintjs.android.aev.utils.concurrency

import java.util.concurrent.CountDownLatch


internal class FromCallbackActions<T> {
    val countDownLatch = CountDownLatch(1)
    @Volatile
    var value: T? = null

    fun emit(value: T) {
        this.value = value
        countDownLatch.countDown()
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T> fromCallback(
    // enabling @BuilderInference will allow us not to specify generic type explicitly
    // @BuilderInference
    block: FromCallbackActions<T>.() -> Unit
): T {
    val callbackActions = FromCallbackActions<T>()

    callbackActions.block()
    callbackActions.countDownLatch.await()

    return callbackActions.value as T // cast to remove unnecessary nullability
}
