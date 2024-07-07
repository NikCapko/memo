package com.nikcapko.memo.core.ui.flow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventFlowWrapper<BaseEvent> : SharedFlowWrapper<BaseEvent> {

    private val eventFlow = MutableSharedFlow<BaseEvent>()

    override fun liveValue() = eventFlow.asSharedFlow()

    override suspend fun update(value: BaseEvent) {
        eventFlow.emit(value)
    }
}