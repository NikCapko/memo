package com.nikcapko.memo.ui.games.selecttranslate

import com.nikcapko.memo.base.ui.command.Command

internal sealed interface SelectTranslateCommand : Command<SelectTranslateCommandReceiver> {

    data object LoadWordsCommand : SelectTranslateCommand {
        override fun execute(receiver: SelectTranslateCommandReceiver) {
            receiver.loadWords()
        }
    }

    data class TranslateClickCommand(private val translate: String) : SelectTranslateCommand {
        override fun execute(receiver: SelectTranslateCommandReceiver) {
            receiver.onTranslateClick(translate)
        }
    }

    data object AnimationEndCommand : SelectTranslateCommand {
        override fun execute(receiver: SelectTranslateCommandReceiver) {
            receiver.onAnimationEnd()
        }
    }

    data object BackPressedCommand : SelectTranslateCommand {
        override fun execute(receiver: SelectTranslateCommandReceiver) {
            receiver.onBackPressed()
        }
    }
}