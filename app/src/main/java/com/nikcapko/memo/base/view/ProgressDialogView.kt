package com.nikcapko.memo.base.view

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ProgressDialogView {
    fun startProgressDialog()
    fun errorProgressDialog(errorMessage: String?)
    fun completeProgressDialog()
}
