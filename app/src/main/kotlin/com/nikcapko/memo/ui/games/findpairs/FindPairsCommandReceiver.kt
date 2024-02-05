package com.nikcapko.memo.ui.games.findpairs

import com.nikcapko.memo.base.ui.command.CommandReceiver

interface FindPairsCommandReceiver : CommandReceiver {
    fun loadWords()
    fun onFindPair(selectedWord: String, selectedTranslate: String)
    fun onBackPressed()
}