package com.nikcapko.memo.ui.words.list

internal sealed interface WordListEvent {
    data class SpeakOutEvent(val word: String) : WordListEvent
    data object ShowClearDatabaseEvent : WordListEvent
    data object ShowNeedMoreWordsEvent : WordListEvent
}
