package com.fingerprintjs.android.aev.utils.concurrency

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.math.min
import com.cloned.github.michaelbull.result.*

private val executor = Executors.newCachedThreadPool()

internal inline fun runInAnotherThread(
    crossinline block: () -> Unit,
) {
    executor.execute { block.invoke() }
}

internal inline fun <T1, T2> runInParallel(
    crossinline block1: () -> T1,
    crossinline block2: () -> T2,
): Pair<Result<T1, Throwable>, Result<T2, Throwable>> {
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
): Triple<Result<T1, Throwable>, Result<T2, Throwable>, Result<T3, Throwable>> {
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
): List<Result<T, Throwable>> {
    return blocks
        .map { block -> executor.submit(Callable { block() }) }
        .map { future -> runCatching { future.get() } }
}

internal inline fun <T, R> Iterable<T>.mapParallel(
    threadCount: Int = runCatching { Runtime.getRuntime().availableProcessors() }.getOr(1),
    crossinline transform: (T) -> R,
): List<R> {
    if (threadCount < 1) {
        throw IllegalArgumentException("mapParallel: threadCount cannot be < 1, got: $threadCount")
    }
    val list = this.toList()
    if (list.isEmpty()) {
        return emptyList()
    }
    val subListSize = (list.size + threadCount - 1) / threadCount
    val subListsCount = (list.size + subListSize - 1) / subListSize
    val subLists =
        (0 until subListsCount)
            .map { subListIndex ->
                list.subList(
                    subListIndex * subListSize,
                    min((subListIndex + 1) * subListSize, list.size)
                )
            }
    val subListMappingLambdas = subLists.map { sublist ->
        { sublist.map { transform(it) } }
    }

    val results = runInParallelVararg(*subListMappingLambdas.toTypedArray())

    return results.map { it.getOrThrow() }.flatten()
}
