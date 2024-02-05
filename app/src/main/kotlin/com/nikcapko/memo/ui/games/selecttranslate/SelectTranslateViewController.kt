package com.nikcapko.memo.ui.games.selecttranslate

interface SelectTranslateViewController {
    fun loadWords()
    fun onTranslateClick(translate: String)
    fun onAnimationEnd()
    fun onBackPressed()
}
