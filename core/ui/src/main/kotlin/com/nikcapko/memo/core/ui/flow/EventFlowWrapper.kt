package com.nikcapko.memo.core.ui.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class EventFlowWrapper<BaseEvent> : SharedFlowWrapper<BaseEvent> {

    private val eventFlow = Channel<BaseEvent>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    override fun liveValue() = eventFlow.receiveAsFlow()

    override suspend fun update(value: BaseEvent) {
        eventFlow.send(value)
    }
}
