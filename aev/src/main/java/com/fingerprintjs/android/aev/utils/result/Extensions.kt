package com.fingerprintjs.android.aev.utils.result

import com.cloned.github.michaelbull.result.*

internal fun <V, E> Result<Result<V, E>, E>.flatten(): Result<V, E> {
    return flatMap { it }
}

internal inline fun <V, E1, E2> Result<Result<V, E1>, E2>.flattenMappingError(transform: (E2) -> E1): Result<V, E1> {
    return mapError(transform).flatten()
}

internal val Result<*, *>.isError: Boolean
    get() = this is Err

internal val Result<*, *>.isOk: Boolean
    get() = this is Ok
