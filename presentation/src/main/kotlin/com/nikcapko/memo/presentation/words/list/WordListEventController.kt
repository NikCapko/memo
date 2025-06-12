package com.nikcapko.memo.presentation.words.list

interface WordListEventController {
    fun speakOut(word: String?)
    fun showClearDatabaseDialog()
    fun showNeedMoreWordsDialog()
}
