package com.nikcapko.memo.presentation.screens.words.details.state

import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.core.ui.BaseState

internal data class WordDetailsState(
    val word: WordModel,
    val isAddNewWord: Boolean,
    val showProgressDialog: Boolean,
    val enableSaveButton: Boolean,
) : BaseState
