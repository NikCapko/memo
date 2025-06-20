package com.nikcapko.memo.presentation.screens.words.list.event

import com.nikcapko.memo.core.ui.BaseEvent

internal sealed interface WordListEvent : BaseEvent {
    data class SpeakOutEvent(val word: String) : WordListEvent
    data object ShowClearDatabaseEvent : WordListEvent
    data object ShowNeedMoreWordsEvent : WordListEvent
}
