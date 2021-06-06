package com.nik.capko.memo.ui.games.phrases

import com.nik.capko.memo.base.view.ProgressMvpView
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PhrasesView : MvpView, ProgressMvpView {
    fun initView(phrase: String, translates: MutableList<String?>?)
    fun showMessage(text: String)
}
