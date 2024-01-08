package com.nikcapko.memo.ui.words.list

interface WordListViewController {
    fun loadWords()
    fun onItemClick(position: Int)
    fun onEnableSound(position: Int)
    fun clearDatabase()
    fun onAddWordClick()
    fun openGamesScreen()
    fun onClearDatabaseClick()
}
