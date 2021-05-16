package com.nik.capko.memo.ui.dictionary

import com.nik.capko.memo.base.view.ProgressMvpView
import com.nik.capko.memo.data.Dictionary
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DictionaryView : MvpView, ProgressMvpView {
    fun showDictionaryList(dictionary: List<Dictionary>)
    fun showLoadingDialog(position: Int)
}
