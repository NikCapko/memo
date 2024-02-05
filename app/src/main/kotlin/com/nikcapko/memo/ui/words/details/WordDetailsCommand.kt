package com.nikcapko.memo.ui.words.details

import com.nikcapko.memo.base.ui.command.Command

internal sealed interface WordDetailsCommand : Command<WordDetailsCommandReceiver> {

    data class SaveWordCommand(
        private val word: String,
        private val translate: String
    ) : WordDetailsCommand {
        override fun execute(receiver: WordDetailsCommandReceiver) {
            receiver.onSaveWord(word, translate)
        }
    }

    data object DeleteWordCommand : WordDetailsCommand {
        override fun execute(receiver: WordDetailsCommandReceiver) {
            receiver.onDeleteWord()
        }
    }

    data class ChangeWordFieldCommand(private val word: String) : WordDetailsCommand {
        override fun execute(receiver: WordDetailsCommandReceiver) {
            receiver.changeWordField(word)
        }
    }

    data class ChangeTranslateFieldCommand(private val translate: String) : WordDetailsCommand {
        override fun execute(receiver: WordDetailsCommandReceiver) {
            receiver.changeTranslateField(translate)
        }
    }
}