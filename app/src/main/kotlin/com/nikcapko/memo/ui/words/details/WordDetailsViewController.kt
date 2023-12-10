package com.nikcapko.memo.ui.words.details

interface WordDetailsViewController {
    fun setArguments(vararg params: Any?)
    fun onSaveWord(wordArg: String, translate: String)
    fun onDeleteWord()
    fun changeWordField(word: String)
    fun changeTranslateField(translate: String)
}