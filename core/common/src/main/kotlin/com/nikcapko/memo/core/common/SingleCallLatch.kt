package com.nikcapko.memo.core.common

import java.util.concurrent.atomic.AtomicBoolean

class SingleCallLatch {
    private val called = AtomicBoolean()

    @Synchronized
    fun considerCalling(action: () -> Unit) {
        val calledBefore = called.getAndSet(true)

        if (calledBefore.not()) {
            action()
        }
    }
}
