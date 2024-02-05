package com.nikcapko.memo.ui.words.list

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.ui.flow.StateFlowWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class WordListStateFlowWrapper @Inject constructor() :
    StateFlowWrapper<DataLoadingViewModelState> {

    private val state = MutableStateFlow<DataLoadingViewModelState?>(null)

    override fun value() = requireNotNull(state.value)

    override fun liveValue() = state.filterNotNull().distinctUntilChanged()

    override fun update(value: DataLoadingViewModelState) {
        state.update { value }
    }
}

