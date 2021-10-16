package com.nikcapko.memo.utils.view.progressview

interface IProgressView {
    fun startLoading()
    fun errorLoading(errorMessage: String?)
    fun completeLoading()
}
