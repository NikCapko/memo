package com.nikcapko.memo.ui.words.list

import ru.ar2code.mutableliveevent.Event
import ru.ar2code.mutableliveevent.EventArgs

internal sealed interface WordListEvent {
    data class SpeakOutEvent(val word: String) : EventArgs<String>(word), WordListEvent
    object ShowClearDatabaseEvent : Event(), WordListEvent
    object ShowNeedMoreWordsEvent : Event(), WordListEvent
}
