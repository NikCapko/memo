package com.nikcapko.memo.ui.words.detail

import com.nikcapko.memo.base.view.ProgressDialogView
import com.nikcapko.memo.data.Word
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface WordDetailView : MvpView, ProgressDialogView {
    fun initView(word: Word?)
    fun sendSuccessResult()
    fun enableSaveButton(enable: Boolean)
}
