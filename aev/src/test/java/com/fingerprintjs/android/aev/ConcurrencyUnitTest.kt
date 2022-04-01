package com.fingerprintjs.android.aev

import com.fingerprintjs.android.aev.utils.runInParallel
import org.junit.Test

import org.junit.Assert.*
import java.lang.Exception

class ConcurrencyUnitTest {

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

        assertTrue(results.first.isFailure)
        assertTrue(results.second.isSuccess)
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
            .mapNotNull { it.getOrNull() }
            .map { listOf(it.first, it.second, it.third) }
            .flatten()

        assertTrue(resultsFlattened.size == tasksCount)
        assertTrue(resultsFlattened.all { it.isSuccess })
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


    companion object {

        private const val THREAD_SLEEP_MS = 1000L
        private const val EPSILON_MS = 200L
    }
}