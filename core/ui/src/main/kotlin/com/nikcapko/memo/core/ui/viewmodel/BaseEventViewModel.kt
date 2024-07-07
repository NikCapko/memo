package com.nikcapko.memo.core.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.ui.BaseEvent
import com.nikcapko.memo.core.ui.flow.EventFlowWrapper
import kotlinx.coroutines.launch

abstract class BaseEventViewModel<T : BaseEvent>(
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel() {

    private val eventFlowWrapper: EventFlowWrapper<T> = EventFlowWrapper<T>()

    val eventFlow = eventFlowWrapper.liveValue()

    fun sendEvent(event: T) {
        viewModelScope.launch(dispatcherProvider.main) {
            eventFlowWrapper.update(event)
        }
    }
}
