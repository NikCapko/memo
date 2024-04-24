package com.nikcapko.memo.core.navigation

import com.nikcapko.memo.core.common.data.Word

interface RootNavigator {
    fun pushWordListScreen()
    fun pushWordDetailScreen(word: Word? = null)
    fun pushGamesScreen()
    fun pushSelectTranslateScreen()
    fun pushFindPairsScreen()
    fun back()
}