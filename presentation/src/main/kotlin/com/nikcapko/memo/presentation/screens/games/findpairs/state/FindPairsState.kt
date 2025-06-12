package com.nikcapko.memo.presentation.screens.games.findpairs.state

import com.nikcapko.memo.core.ui.BaseState

internal sealed interface FindPairsState : BaseState {
    data object None : FindPairsState
    data object Loading : FindPairsState
    data class Error(val errorMessage: String) : FindPairsState
    data class Success(
        val wordList: List<String>,
        val translateList: List<String>,
    ) : FindPairsState
}
