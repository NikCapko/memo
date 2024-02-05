package com.nikcapko.memo.base.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.base.ui.BaseEvent
import com.nikcapko.memo.base.ui.flow.EventFlowWrapper
import kotlinx.coroutines.launch

internal abstract class BaseEventViewModel<T : BaseEvent>(
    private val eventFlowWrapper: EventFlowWrapper<T>,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel() {

    val eventFlow = eventFlowWrapper.liveValue()

    fun sendEvent(event: T) {
        viewModelScope.launch(dispatcherProvider.main) {
            eventFlowWrapper.update(event)
        }
    }
}