package com.fingerprintjs.android.aev.annotations

/**
 * Denotes that the annotated method should only be called on a worker thread.
 * If the annotated element is a class, then all methods in the class should be called
 * on a worker thread.
 *
 * This is an alternative to androidx.annotation.WorkerThread.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.CLASS,
)
internal annotation class WorkerThread