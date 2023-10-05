package com.nikcapko.memo.base.view

interface ProgressDialogView {
    fun startProgressDialog()
    fun errorProgressDialog(errorMessage: String?)
    fun completeProgressDialog()
}
