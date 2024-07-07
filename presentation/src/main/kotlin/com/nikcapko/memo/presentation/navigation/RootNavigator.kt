package com.nikcapko.memo.presentation.navigation

import com.nikcapko.memo.core.data.Word

interface RootNavigator {
    fun pushWordListScreen()
    fun pushWordDetailScreen(word: Word? = null)
    fun pushGamesScreen()
    fun pushSelectTranslateScreen()
    fun pushFindPairsScreen()
    fun back()
}