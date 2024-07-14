package com.nikcapko.memo.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nikcapko.memo.core.common.SingleCallLatch

abstract class LazyViewModel : ViewModel() {

    private val viewCreatedLatch = SingleCallLatch()

    fun onCreateViewModel() {
        viewCreatedLatch.considerCalling(::onViewFirstCreated)
    }

    /** Функция будет вызвана при первом создании вью */
    open fun onViewFirstCreated() {
        // Nothing by default
    }
}
