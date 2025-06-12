package com.nikcapko.memo.presentation.words.details

import com.nikcapko.memo.core.ui.BaseEvent

internal sealed interface WordDetailsEvent : BaseEvent {
    data object CloseScreenEvent : WordDetailsEvent
}
