package com.nikcapko.memo.ui.games.find_pairs

import com.nikcapko.memo.base.ui.SharedFlowWrapper
import com.nikcapko.memo.base.ui.StateFlowWrapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class FindPairsStateFlowWrapper @Inject constructor() :
    StateFlowWrapper<FindPairsState> {

    private val state = MutableStateFlow<FindPairsState?>(null)

    override fun value() = requireNotNull(state.value)

    override fun liveValue() = state.filterNotNull().distinctUntilChanged()

    override fun update(value: FindPairsState) {
        state.update { value }
    }
}

internal class FindPairsEventFlowWrapper @Inject constructor() : SharedFlowWrapper<FindPairsEvent> {

    private val state = MutableSharedFlow<FindPairsEvent>()

    override fun liveValue() = state.asSharedFlow()

    override suspend fun update(value: FindPairsEvent) {
        state.emit(value)
    }
}

sealed interface FindPairsEvent {
    data class FindPairResultEvent(val success: Boolean) : FindPairsEvent
    data object EndGameEvent : FindPairsEvent
}
