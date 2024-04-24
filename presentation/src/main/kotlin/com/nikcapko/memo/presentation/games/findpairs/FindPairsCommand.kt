package com.nikcapko.memo.presentation.games.findpairs

import com.nikcapko.memo.core.ui.command.Command

internal sealed interface FindPairsCommand : Command<FindPairsCommandReceiver> {

    data object LoadWordsCommand : FindPairsCommand {
        override fun execute(receiver: FindPairsCommandReceiver) {
            receiver.loadWords()
        }
    }

    data class FindPairCommand(
        private val selectedWord: String,
        private val selectedTranslate: String,
    ) : FindPairsCommand {
        override fun execute(receiver: FindPairsCommandReceiver) {
            receiver.onFindPair(selectedWord, selectedTranslate)
        }
    }

    data object BackPressedCommand :
        FindPairsCommand {
        override fun execute(receiver: FindPairsCommandReceiver) {
            receiver.onBackPressed()
        }
    }
}