package com.nikcapko.memo.presentation.navigation

import com.nikcapko.domain.model.WordModel

interface RootNavigator {
    fun pushWordListScreen()
    fun pushWordDetailScreen(word: WordModel? = null)
    fun pushGamesScreen()
    fun pushSelectTranslateScreen()
    fun pushFindPairsScreen()
    fun back()
}
