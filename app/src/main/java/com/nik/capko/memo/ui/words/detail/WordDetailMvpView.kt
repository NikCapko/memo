package com.nik.capko.memo.ui.words.detail

import com.nik.capko.memo.base.view.ProgressDialogView
import com.nik.capko.memo.data.Word
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface WordDetailMvpView : MvpView, ProgressDialogView {
    fun initView(word: Word)
    fun sendSuccessResult()
    fun onCloseScreen()
}
