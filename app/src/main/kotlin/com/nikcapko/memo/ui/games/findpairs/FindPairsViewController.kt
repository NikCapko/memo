package com.nikcapko.memo.ui.games.findpairs

interface FindPairsViewController {
    fun loadWords()
    fun onFindPair(selectedWord: String, selectedTranslate: String)
    fun onBackPressed()
}