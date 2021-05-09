package com.nik.capko.memo.ui.words.list

import com.nik.capko.memo.base.view.ProgressMvpView
import com.nik.capko.memo.data.Word
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface WordListMvpView : MvpView, ProgressMvpView {
    fun showWords(wordsList: List<Word>)
    fun showWordDetail(word: Word?)
    fun openAddWordScreen()
}
