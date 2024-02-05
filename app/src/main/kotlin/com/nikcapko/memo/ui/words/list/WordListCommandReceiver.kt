package com.nikcapko.memo.ui.words.list

import com.nikcapko.memo.base.ui.command.CommandReceiver

internal interface WordListCommandReceiver : CommandReceiver {
    fun loadWords()
    fun onItemClick(position: Int)
    fun onEnableSound(position: Int)
    fun clearDatabase()
    fun onAddWordClick()
    fun openGamesScreen()
    fun onClearDatabaseClick()
}