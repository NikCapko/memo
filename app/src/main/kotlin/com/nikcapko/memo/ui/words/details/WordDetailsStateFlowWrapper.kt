package com.nikcapko.memo.ui.words.details

import com.nikcapko.memo.base.ui.flow.StateFlowWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class WordDetailsStateFlowWrapper @Inject constructor() :
    StateFlowWrapper<WordDetailsViewState> {

    private val stateFlow = MutableStateFlow<WordDetailsViewState?>(null)

    override fun value() = requireNotNull(stateFlow.value)

    override fun liveValue() = stateFlow.filterNotNull().distinctUntilChanged()

    override fun update(value: WordDetailsViewState) {
        stateFlow.update { value }
    }
}
