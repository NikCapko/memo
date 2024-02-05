package com.nikcapko.memo.ui.games.list

import com.nikcapko.memo.base.ui.command.CommandReceiver

interface GamesCommandReceiver : CommandReceiver {
    fun onItemClick(position: Int)
}
