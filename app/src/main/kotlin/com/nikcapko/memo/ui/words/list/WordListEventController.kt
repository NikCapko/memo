package com.nikcapko.memo.ui.words.list

interface WordListEventController {
    fun speakOut(word: String?)
    fun showClearDatabaseDialog()
    fun showNeedMoreWordsDialog()
}