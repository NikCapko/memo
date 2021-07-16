package com.nikcapko.memo.ui.dictionary

import com.nikcapko.memo.base.view.ProgressDialogView
import com.nikcapko.memo.base.view.ProgressMvpView
import com.nikcapko.memo.data.Dictionary
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DictionaryView : MvpView, ProgressMvpView, ProgressDialogView {
    fun showDictionaryList(dictionary: List<Dictionary>)
    fun showLoadingDialog(position: Int)
    fun sendSuccessResult()
}
