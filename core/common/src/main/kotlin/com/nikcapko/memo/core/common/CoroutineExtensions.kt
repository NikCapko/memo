package com.nikcapko.memo.core.common

import kotlinx.coroutines.CoroutineExceptionHandler

fun exceptionHandler(errorBlock: (Throwable) -> Unit) =
    CoroutineExceptionHandler { _, exception ->
        errorBlock(exception)
    }

val emptyExceptionHandler = CoroutineExceptionHandler { _, exception -> }
