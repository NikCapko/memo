package com.nikcapko.memo.ui.games.find_pairs

interface FindPairsViewController {
    fun loadWords()
    fun onFindPair(selectedWord: String, selectedTranslate: String)
    fun onBackPressed()
}