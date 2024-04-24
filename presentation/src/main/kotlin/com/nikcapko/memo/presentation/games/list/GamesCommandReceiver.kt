package com.nikcapko.memo.presentation.games.list

import com.nikcapko.memo.core.ui.command.CommandReceiver

interface GamesCommandReceiver : CommandReceiver {
    fun onItemClick(position: Int)
}
