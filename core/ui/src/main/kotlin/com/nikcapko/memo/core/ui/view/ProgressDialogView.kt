package com.nikcapko.memo.core.ui.view

interface ProgressDialogView {
    fun startProgressDialog()
    fun errorProgressDialog(errorMessage: String?)
    fun completeProgressDialog()
}
