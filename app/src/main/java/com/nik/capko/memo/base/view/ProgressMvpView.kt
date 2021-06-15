package com.nik.capko.memo.base.view

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ProgressMvpView {
    fun startLoading()
    fun errorLoading(errorMessage: String?)
    fun completeLoading()
    fun onRetry()
}
