package com.nikcapko.memo.ui.words.list

import com.nikcapko.memo.base.ui.command.Command

internal sealed interface WordListCommand : Command<WordListCommandReceiver> {

    data class ItemClickCommand(private val position: Int) : WordListCommand {
        override fun execute(receiver: WordListCommandReceiver) {
            receiver.onItemClick(position)
        }
    }

    data class EnableSoundCommand(private val position: Int) : WordListCommand {
        override fun execute(receiver: WordListCommandReceiver) {
            receiver.onEnableSound(position)
        }
    }

    data object UpdateItemsCommand : WordListCommand {
        override fun execute(receiver: WordListCommandReceiver) {
            receiver.loadWords()
        }
    }

    data object OpenGamesScreenCommand : WordListCommand {
        override fun execute(receiver: WordListCommandReceiver) {
            receiver.openGamesScreen()
        }
    }

    data object ClearDatabaseClickCommand : WordListCommand {
        override fun execute(receiver: WordListCommandReceiver) {
            receiver.onClearDatabaseClick()
        }
    }

    data object ClearDatabaseConfirmCommand : WordListCommand {
        override fun execute(receiver: WordListCommandReceiver) {
            receiver.clearDatabase()
        }
    }

    data object AddWordClickCommand : WordListCommand {
        override fun execute(receiver: WordListCommandReceiver) {
            receiver.onAddWordClick()
        }
    }
}