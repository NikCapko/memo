package com.nikcapko.memo.base.ui

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.base.coroutines.DispatcherProvider
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