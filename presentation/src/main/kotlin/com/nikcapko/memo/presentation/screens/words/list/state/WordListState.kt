package com.nikcapko.memo.presentation.screens.words.list.state

import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.core.ui.BaseState

internal sealed interface WordListState : BaseState {
    data object None : WordListState
    data object Loading : WordListState
    data class Error(val errorMessage: String) : WordListState
    data class Success(val words: List<WordModel>) : WordListState
}
