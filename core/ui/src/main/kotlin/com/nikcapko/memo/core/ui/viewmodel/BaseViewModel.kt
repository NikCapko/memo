package com.nikcapko.memo.core.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.core.ui.BaseEvent
import com.nikcapko.memo.core.ui.BaseState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseState, T : BaseEvent> : LazyViewModel() {

    private val _state by lazy { MutableStateFlow(createInitialState()) }

    val state by lazy { _state.asStateFlow() }

    abstract fun createInitialState(): S

    fun updateState(func: (S) -> S) {
        _state.update { func(it) }
    }

    private val _eventFlow: Channel<T> = Channel(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    val eventFlow = _eventFlow.receiveAsFlow()

    fun sendEvent(event: T) {
        viewModelScope.launch {
            _eventFlow.send(event)
        }
    }
}
