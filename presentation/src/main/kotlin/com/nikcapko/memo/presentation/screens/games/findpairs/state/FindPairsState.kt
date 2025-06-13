package com.nikcapko.memo.presentation.screens.games.findpairs.state

import com.nikcapko.memo.core.ui.BaseState

internal sealed interface FindPairsState : BaseState {
    data object None : FindPairsState
    data object Loading : FindPairsState
    data class Error(val errorMessage: String) : FindPairsState
    data class Success(
        val wordList: List<Item>,
        val translateList: List<Item>,
        val selectWord: Item? = null,
        val selectTranslate: Item? = null,
    ) : FindPairsState

    data object EndGame : FindPairsState

    data class Item(
        val id: String,
        val value: String,
        val checked: Boolean = false,
        val isVisible: Boolean = true,
    )
}

internal fun FindPairsState.mapSuccessState(
    mapper: (FindPairsState.Success) -> FindPairsState,
): FindPairsState {
    return when (this) {
        is FindPairsState.Success -> mapper(this)
        else -> this
    }
}
