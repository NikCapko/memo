package com.nikcapko.memo.utils.extensions

inline fun <T> lazyUnsafe(noinline initializer: () -> T) =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        initializer.invoke()
    }
