@file:Suppress("MemberVisibilityCanBePrivate")

package com.fingerprintjs.android.aev.errors

import java.io.IOException

//region Public

public sealed interface Error {
    public val description: String
        get() = getDefaultDescription()
}

/*
All Errors are of one of the following type, so it's safe to write exhausted when expressions
like the following:
    fun f(e: Error) {
        val v = when (e) {
            is NetworkError -> Unit
            is ApiError -> Unit
            is InternalError -> Unit
        }
    }
*/
public sealed interface NetworkError : Error
public sealed interface ApiError : Error
public sealed interface InternalError : Error

//endregion

//region Interfaces for gathering different errors for domain-specific purposes

internal sealed interface ApiInteractorGetTokenError: Error
internal sealed interface HttpClientError: Error, ApiInteractorGetTokenError

//endregion

//region Errors

internal class UnknownInternalError(val t: Throwable) : InternalError, HttpClientError {
    override val description: String
        get() = getDescription(t.toString())
}

internal object UnauthorizedError : ApiError
internal class UnknownApiError(
    val errorCode: Int,
    val body: String // UTF-8 decoded
) : ApiError, HttpClientError {
    override val description: String
        get() = getDescription("Error code: $errorCode. Body: $body")
}
internal class ParseError(
    val fromText: String,
    val toClass: Class<*>
): ApiError, ApiInteractorGetTokenError {
    override val description: String
        get() = getDescription("Could not parse text to ${toClass.canonicalName}. Text: $fromText")
}

internal object TimeoutError : NetworkError, HttpClientError
internal class UnknownNetworkError(val cause: IOException) : NetworkError, HttpClientError {
    override val description: String
        get() = getDescription(cause.toString())
}

//endregion

//region Helper functions

private fun Error.getDefaultDescription(): String =
    "Error ${this.javaClass.canonicalName} occurred."

private fun Error.getDescription(internalDescription: String): String =
    getDefaultDescription() + " Description: $internalDescription"

//endregion
