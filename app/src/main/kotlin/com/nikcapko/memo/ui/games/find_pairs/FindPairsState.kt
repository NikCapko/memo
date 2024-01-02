package com.nikcapko.memo.ui.games.find_pairs

import com.nikcapko.core.viewmodel.DataLoadingViewModelState

internal data class FindPairsState(
    val dataLoadingViewModelState: DataLoadingViewModelState,
    val wordList: List<String>,
    val translateList: List<String>,
    val wordsCount: Int,
)
