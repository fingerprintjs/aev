package com.fingerprintjs.android.aev

import com.fingerprintjs.android.aev.utils.concurrency.mapParallel
import com.fingerprintjs.android.aev.utils.concurrency.runInParallel
import com.fingerprintjs.android.aev.utils.result.isError
import com.fingerprintjs.android.aev.utils.result.isOk
import com.cloned.github.michaelbull.result.get
import org.junit.Test

import org.junit.Assert.*
import kotlin.Exception

class ThreadingUnitTest {

    @Test
    fun executesInParallel() {
        val startTime = System.currentTimeMillis()

        runInParallel(
            { Thread.sleep(THREAD_SLEEP_MS) },
            { Thread.sleep(THREAD_SLEEP_MS) },
        )

        assertTrue((System.currentTimeMillis() - startTime) < THREAD_SLEEP_MS + EPSILON_MS)
    }

    @Test
    fun parallelExecutionErrorCaught() {
        val results = runInParallel(
            {
                Thread.sleep(THREAD_SLEEP_MS)
                throw Exception()
            },
            { Thread.sleep(THREAD_SLEEP_MS) },
        )

        assertTrue(results.first.isError)
        assertTrue(results.second.isOk)
    }

    @Test
    fun executesInParallelMoreTasks() {
        val startTime = System.currentTimeMillis()
        val tasksCount = 6

        val results = runInParallel(
            {
                runInParallel(
                    { Thread.sleep(THREAD_SLEEP_MS) },
                    { Thread.sleep(THREAD_SLEEP_MS) },
                    { Thread.sleep(THREAD_SLEEP_MS) },
                )
            },
            {
                runInParallel(
                    { Thread.sleep(THREAD_SLEEP_MS) },
                    { Thread.sleep(THREAD_SLEEP_MS) },
                    { Thread.sleep(THREAD_SLEEP_MS) },
                )
            },
        )

        val resultsFlattened = results
            .run { arrayOf(first, second) }
            .mapNotNull { it.get() }
            .map { listOf(it.first, it.second, it.third) }
            .flatten()

        assertTrue(resultsFlattened.size == tasksCount)
        assertTrue(resultsFlattened.all { it.isOk })
        assertTrue((System.currentTimeMillis() - startTime) < THREAD_SLEEP_MS + EPSILON_MS)
    }

    @Test
    fun recursiveParallelExecutionNoDeadlock() {
        runInParallel(
            {
                runInParallel(
                    {
                        runInParallel(
                            {
                                runInParallel(
                                    {
                                        runInParallel(
                                            {
                                                Thread.sleep(THREAD_SLEEP_MS)
                                            },
                                            {
                                                // do nothing
                                            }
                                        )
                                    },
                                    {
                                        // do nothing
                                    }
                                )
                            },
                            {
                                // do nothing
                            }
                        )
                    },
                    {
                        // do nothing
                    }
                )
            },
            {
                // do nothing
            }
        )
    }

    @Test
    fun mapParallelTest() {
        fun testMapping(listSize: Int, threadCount: Int) {
            val l = List(listSize) { index -> index }
            val lMapped = l.map { it + 1 }
            val lMappedP = l.mapParallel(threadCount = threadCount) { it + 1 }
            assertTrue(lMapped == lMappedP)
        }

        testMapping(listSize = 10_000, threadCount = 1)
        testMapping(listSize = 10_000, threadCount = 2)
        testMapping(listSize = 10_000, threadCount = 3)
        testMapping(listSize = 10_000, threadCount = 4)

        testMapping(listSize = 0, threadCount = 1)
        testMapping(listSize = 0, threadCount = 2)
        testMapping(listSize = 1, threadCount = 1)
        testMapping(listSize = 1, threadCount = 2)
        testMapping(listSize = 2, threadCount = 1)
        testMapping(listSize = 2, threadCount = 2)
        testMapping(listSize = 2, threadCount = 3)

        val resMapWithException = runCatching {
            val l = List(1) { it }
            l.mapParallel { if (it == 0) throw Exception() else it }
        }
        assertTrue(resMapWithException.isFailure)

        val resIllegalArg = runCatching {
            emptyList<Int>().mapParallel(threadCount = 0) { it }
        }
        assertTrue(resIllegalArg.exceptionOrNull() is IllegalArgumentException)
    }

    companion object {

        private const val THREAD_SLEEP_MS = 1000L
        private const val EPSILON_MS = 200L
    }
}