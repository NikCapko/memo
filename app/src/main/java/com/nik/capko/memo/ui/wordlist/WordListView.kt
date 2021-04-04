package com.nik.capko.memo.ui.wordlist

import com.nik.capko.memo.db.Word
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface WordListView : MvpView {
    fun showWords(wordsList: List<Word>)
}
