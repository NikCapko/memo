package com.nikcapko.memo.presentation.words.details

import com.nikcapko.memo.core.data.Word

internal data class WordDetailsViewState(
    val word: Word?,
    val showProgressDialog: Boolean,
    val enableSaveButton: Boolean,
)
