package com.nikcapko.memo.utils.extensions

inline fun <T> androidLazy(noinline initializer: () -> T) =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        initializer.invoke()
    }
