package com.nikcapko.memo.ui.games.phrases

import com.nikcapko.memo.base.view.ProgressMvpView
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PhrasesView : MvpView, ProgressMvpView {
    fun initView(phrase: String, translates: List<String>?)
    fun showMessage(text: String)
}
