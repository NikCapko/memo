package com.nikcapko.memo.utils.extensions

inline fun <T> lazyAndroid(noinline initializer: () -> T) =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        initializer.invoke()
    }
