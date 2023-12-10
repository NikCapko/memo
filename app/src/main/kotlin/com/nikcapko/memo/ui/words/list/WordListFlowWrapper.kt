package com.nikcapko.memo.ui.words.list

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import kotlinx.coroutines.flow.Flow

internal interface WordListFlowWrapper {
    val state: Flow<DataLoadingViewModelState>
    val event: Flow<WordListEvent>
}
