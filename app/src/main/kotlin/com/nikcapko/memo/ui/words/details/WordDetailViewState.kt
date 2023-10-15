package com.nikcapko.memo.ui.words.details

import com.nikcapko.memo.data.Word

internal data class WordDetailViewState(
    val word: Word?,
    val showProgressDialog: Boolean,
    val enableSaveButton: Boolean,
)
