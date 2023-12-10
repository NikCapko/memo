package com.nikcapko.memo.ui.games.find_pairs

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import kotlinx.coroutines.flow.Flow

interface FindPairsFlowWrapper {
    val state: Flow<DataLoadingViewModelState>
    val eventFlow: Flow<FindPairsEvent>
}