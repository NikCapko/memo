package com.nikcapko.memo.presentation.games.findpairs

import com.nikcapko.memo.core.ui.command.CommandReceiver

interface FindPairsCommandReceiver : CommandReceiver {
    fun loadWords()
    fun onFindPair(selectedWord: String, selectedTranslate: String)
    fun onBackPressed()
}