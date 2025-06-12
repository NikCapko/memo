package com.nikcapko.memo.core.common

import kotlinx.coroutines.CoroutineExceptionHandler

inline fun exceptionHandler(crossinline errorBlock: (Throwable) -> Unit) =
    CoroutineExceptionHandler { _, exception ->
        errorBlock(exception)
    }

val emptyExceptionHandler = CoroutineExceptionHandler { _, exception -> }
