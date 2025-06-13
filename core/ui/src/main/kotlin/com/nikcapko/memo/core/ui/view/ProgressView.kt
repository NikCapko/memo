package com.nikcapko.memo.core.ui.view

interface ProgressView {
    fun startLoading()
    fun errorLoading(errorMessage: String?)
    fun completeLoading()
    fun onRetry()
}
