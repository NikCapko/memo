package com.nikcapko.memo.ui.games.list

import com.nikcapko.memo.base.ui.command.Command

interface GamesCommand : Command<GamesCommandReceiver> {
    data class ItemClickCommand(private val position: Int) : GamesCommand {
        override fun execute(receiver: GamesCommandReceiver) {
            receiver.onItemClick(position)
        }
    }
}