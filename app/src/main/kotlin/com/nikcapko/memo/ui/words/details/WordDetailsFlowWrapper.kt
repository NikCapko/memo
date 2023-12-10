package com.nikcapko.memo.ui.words.details

import com.nikcapko.memo.data.Word
import kotlinx.coroutines.flow.Flow

internal interface WordDetailsFlowWrapper {
    val wordState: Flow<Word>
    val progressLoadingState: Flow<Boolean>
    val enableSaveButtonState: Flow<Boolean>
    val eventFlow: Flow<WordDetailsEvent>
}
