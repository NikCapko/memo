package com.nikcapko.memo.core.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.core.ui.BaseEvent
import com.nikcapko.memo.core.ui.flow.EventFlowWrapper
import kotlinx.coroutines.launch

abstract class BaseEventViewModel<T : BaseEvent> : LazyViewModel() {

    private val eventFlowWrapper: EventFlowWrapper<T> = EventFlowWrapper()

    val eventFlow = eventFlowWrapper.liveValue()

    fun sendEvent(event: T) {
        viewModelScope.launch {
            eventFlowWrapper.update(event)
        }
    }
}
