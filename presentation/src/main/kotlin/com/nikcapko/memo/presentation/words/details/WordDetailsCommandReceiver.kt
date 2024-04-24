package com.nikcapko.memo.presentation.words.details

import com.nikcapko.memo.core.ui.command.CommandReceiver

internal interface WordDetailsCommandReceiver : CommandReceiver {
    fun onSaveWord(wordArg: String, translate: String)
    fun onDeleteWord()
    fun changeWordField(word: String)
    fun changeTranslateField(translate: String)
}