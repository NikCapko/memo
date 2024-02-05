package com.nikcapko.memo.base.ui.flow

import com.nikcapko.memo.base.ui.BaseEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

internal class EventFlowWrapper<T : BaseEvent> @Inject constructor() : SharedFlowWrapper<T> {

    private val eventFlow = MutableSharedFlow<T>()

    override fun liveValue() = eventFlow.asSharedFlow()

    override suspend fun update(value: T) {
        eventFlow.emit(value)
    }
}