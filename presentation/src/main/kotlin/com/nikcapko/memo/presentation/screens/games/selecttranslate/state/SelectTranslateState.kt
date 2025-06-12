package com.nikcapko.memo.presentation.screens.games.selecttranslate.state

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.ui.BaseState

internal sealed interface SelectTranslateState : BaseState {
    data object None : SelectTranslateState
    data object Loading : SelectTranslateState
    data class Error(val errorMessage: String) : SelectTranslateState
    data class Success(
        val words: List<WordModel>,
        val screenState: SelectTranslateScreenState,
        val counter: Int = 0,
        val errorCounter: Int = 0,
        val successCounter: Int = 0,
    ) : SelectTranslateState
}

internal sealed interface SelectTranslateScreenState {
    data object None : SelectTranslateScreenState
    data class SelectWord(
        val word: WordModel,
        val translates: List<String>,
    ) : SelectTranslateScreenState

    data object Error : SelectTranslateScreenState
    data object Success : SelectTranslateScreenState
    data class Result(
        val successCount: Int,
        val errorCount: Int,
    ) : SelectTranslateScreenState
}

internal fun SelectTranslateState.mapSuccessState(
    mapper: (SelectTranslateState.Success) -> SelectTranslateState,
): SelectTranslateState {
    return when (this) {
        is SelectTranslateState.Success -> mapper(this)
        else -> this
    }
}

internal fun SelectTranslateScreenState.mapSelectWordState(
    mapper: (SelectTranslateScreenState.SelectWord) -> SelectTranslateScreenState,
): SelectTranslateScreenState {
    return when (this) {
        is SelectTranslateScreenState.SelectWord -> mapper(this)
        else -> this
    }
}
