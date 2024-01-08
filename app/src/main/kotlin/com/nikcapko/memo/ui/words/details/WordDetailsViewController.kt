package com.nikcapko.memo.ui.words.details

interface WordDetailsViewController {
    fun onSaveWord(wordArg: String, translate: String)
    fun onDeleteWord()
    fun changeWordField(word: String)
    fun changeTranslateField(translate: String)
}