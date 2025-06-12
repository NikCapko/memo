package com.nikcapko.memo.presentation.screens.words.details.event

import com.nikcapko.memo.core.ui.BaseEvent

internal sealed interface WordDetailsEvent : BaseEvent {
    data object CloseScreenEvent : WordDetailsEvent
}
