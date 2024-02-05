package com.nikcapko.memo.ui.games.selecttranslate

import com.nikcapko.memo.base.ui.command.CommandReceiver

interface SelectTranslateCommandReceiver: CommandReceiver {
    fun loadWords()
    fun onTranslateClick(translate: String)
    fun onAnimationEnd()
    fun onBackPressed()
}
