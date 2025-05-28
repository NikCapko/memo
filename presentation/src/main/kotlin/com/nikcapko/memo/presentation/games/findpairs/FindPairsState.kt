package com.nikcapko.memo.presentation.games.findpairs

import com.nikcapko.memo.core.ui.viewmodel.DataLoadingViewModelState

internal data class FindPairsState(
    val dataLoadingViewModelState: DataLoadingViewModelState,
    val wordList: List<String>,
    val translateList: List<String>,
    val wordsCount: Int,
)
