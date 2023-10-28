package com.nikcapko.memo.navigation

import com.nikcapko.memo.data.Word

internal interface Navigator {
    fun pushWordListScreen()
    fun pushWordDetailScreen(word: Word? = null)
    fun pushGamesScreen()
    fun pushSelectTranslateScreen()
    fun pushFindPairsScreen()
    fun back()
}