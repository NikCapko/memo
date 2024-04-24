package com.nikcapko.memo.core.ui.progressview

interface ProgressViewInterface {
    fun startLoading()
    fun errorLoading(errorMessage: String?)
    fun completeLoading()
}
