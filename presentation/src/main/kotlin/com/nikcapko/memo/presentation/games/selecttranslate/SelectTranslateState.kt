package com.nikcapko.memo.presentation.games.selecttranslate

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.ui.BaseState

internal sealed interface SelectTranslateState : BaseState {
    data object None : SelectTranslateState
    data object Loading : SelectTranslateState
    data class Error(val errorMessage: String) : SelectTranslateState
    data class Success(val word: WordModel, val translate: List<String>) : SelectTranslateState
}
