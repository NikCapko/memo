package com.nikcapko.memo.ui.games.select_translate

interface SelectTranslateViewController {
    fun loadWords()
    fun onTranslateClick(translate: String)
    fun onAnimationEnd()
    fun onBackPressed()
}
