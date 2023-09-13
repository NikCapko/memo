package com.nikcapko.memo.base.view

interface ProgressView {
    fun startLoading()
    fun errorLoading(errorMessage: String?)
    fun completeLoading()
    fun onRetry()
}
