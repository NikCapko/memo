package com.nikcapko.memo.ui.games.select_translate

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.ui.SharedFlowWrapper
import com.nikcapko.memo.base.ui.StateFlowWrapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class SelectTranslateStateFlowWrapper @Inject constructor() :
    StateFlowWrapper<DataLoadingViewModelState> {

    private val stateFlow = MutableStateFlow<DataLoadingViewModelState?>(null)

    override fun value() = requireNotNull(stateFlow.value)

    override fun liveValue() = stateFlow.filterNotNull().distinctUntilChanged()

    override fun update(value: DataLoadingViewModelState) {
        stateFlow.update { value }
    }
}

internal class SelectTranslateEventFlowWrapper @Inject constructor() :
    SharedFlowWrapper<SelectTranslateEvent> {

    private val eventFlow = MutableSharedFlow<SelectTranslateEvent>()

    override fun liveValue() = eventFlow.asSharedFlow()

    override suspend fun update(value: SelectTranslateEvent) {
        eventFlow.emit(value)
    }
}
