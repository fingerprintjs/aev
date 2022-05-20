package com.fingerprintjs.android.aev

import com.fingerprintjs.android.aev.errors.ApiError
import com.fingerprintjs.android.aev.errors.Error
import com.fingerprintjs.android.aev.errors.InternalError
import com.fingerprintjs.android.aev.errors.NetworkError

class ErrorsUnitTest {

    // We want the build to fail if we do not provide an exhausting set of interfaces for our errors
    @Suppress("UNUSED_VARIABLE")
    fun publicErrorsAreExhausting(e: Error) {
        val v = when (e) {
            is NetworkError,
            is ApiError,
            is InternalError -> {
            }
        }
    }
}