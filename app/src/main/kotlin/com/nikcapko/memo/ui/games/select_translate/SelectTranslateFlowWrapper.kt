package com.nikcapko.memo.ui.games.select_translate

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import kotlinx.coroutines.flow.Flow

internal interface SelectTranslateFlowWrapper {
    val state: Flow<DataLoadingViewModelState>
    val eventFlow: Flow<SelectTranslateEvent>
}
