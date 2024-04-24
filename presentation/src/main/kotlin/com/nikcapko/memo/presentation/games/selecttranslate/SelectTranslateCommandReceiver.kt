package com.nikcapko.memo.presentation.games.selecttranslate

import com.nikcapko.memo.core.ui.command.CommandReceiver

interface SelectTranslateCommandReceiver : CommandReceiver {
    fun loadWords()
    fun onTranslateClick(translate: String)
    fun onAnimationEnd()
    fun onBackPressed()
}
