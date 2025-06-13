package com.nikcapko.memo.core.common

inline fun <T> androidLazy(noinline initializer: () -> T) =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        initializer.invoke()
    }
