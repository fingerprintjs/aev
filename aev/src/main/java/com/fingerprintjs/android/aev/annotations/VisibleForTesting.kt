package com.fingerprintjs.android.aev.annotations

/**
 * Denotes that the class, method or field has its visibility relaxed, so that it is more widely
 * visible than otherwise necessary to make code testable.
 *
 * This is an alternative to androidx.annotation.VisibleForTesting. Adopted to Kotlin.
 *
 *
 * You can optionally specify what the visibility <b>should</b> have been if not for
 * testing.
 * Example:
 * ```
 *  VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
 *  public fun printDiagnostics() { ... }
 * ```
 *
 * If not specified, the intended visibility is assumed to be private.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
)
internal annotation class VisibleForTesting(
    /**
     * The visibility the annotated element would have if it did not need to be made visible for
     * testing.
     */
    val otherwise: Otherwise = Otherwise.PRIVATE
) {
    enum class Otherwise {
        /**
         * The annotated element would have "private" visibility
         */
        PRIVATE,

        /**
         * The annotated element would have "internal" visibility
         */
        INTERNAL,

        /**
         * The annotated element would have "protected" visibility
         */
        PROTECTED,

        /**
         * The annotated element should never be called from production code, only from tests.
         * This is equivalent to `@RestrictTo.Scope.TESTS`.
         */
        NONE,
    }
}
