package com.nikcapko.memo.ui.words.details

sealed interface WordDetailsEvent {
    data object CloseScreenEvent : WordDetailsEvent
}
