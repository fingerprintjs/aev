package com.fingerprintjs.android.aev.utils.concurrency

import java.util.concurrent.Callable
import java.util.concurrent.Executors

private val executor = Executors.newCachedThreadPool()

internal inline fun <T1, T2> runInParallel(
    crossinline block1: () -> T1,
    crossinline block2: () -> T2,
): Pair<Result<T1>, Result<T2>> {
    val f1 = executor.submit(Callable { block1() })
    val f2 = executor.submit(Callable { block2() })
    return Pair(
        runCatching { f1.get() },
        runCatching { f2.get() },
    )
}

internal inline fun <T1, T2, T3> runInParallel(
    crossinline block1: () -> T1,
    crossinline block2: () -> T2,
    crossinline block3: () -> T3,
): Triple<Result<T1>, Result<T2>, Result<T3>> {
    val f1 = executor.submit(Callable { block1() })
    val f2 = executor.submit(Callable { block2() })
    val f3 = executor.submit(Callable { block3() })
    return Triple(
        runCatching { f1.get() },
        runCatching { f2.get() },
        runCatching { f3.get() },
    )
}

internal fun <T> runInParallelVararg(
    vararg blocks: () -> T,
): List<Result<T>> {
    return blocks
        .map { block -> executor.submit(Callable { block() }) }
        .map { future -> runCatching { future.get() } }
}
