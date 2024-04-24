package com.nikcapko.memo.presentation.games.list

import com.nikcapko.memo.core.ui.command.Command

interface GamesCommand : Command<GamesCommandReceiver> {
    data class ItemClickCommand(private val position: Int) : GamesCommand {
        override fun execute(receiver: GamesCommandReceiver) {
            receiver.onItemClick(position)
        }
    }
}