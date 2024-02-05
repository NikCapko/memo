package com.nikcapko.memo.ui.words.details

import com.nikcapko.memo.base.ui.command.CommandReceiver

internal interface WordDetailsCommandReceiver : CommandReceiver {
    fun onSaveWord(wordArg: String, translate: String)
    fun onDeleteWord()
    fun changeWordField(word: String)
    fun changeTranslateField(translate: String)
}