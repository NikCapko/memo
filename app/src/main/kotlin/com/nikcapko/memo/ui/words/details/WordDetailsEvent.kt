package com.nikcapko.memo.ui.words.details

import ru.ar2code.mutableliveevent.Event

sealed interface WordDetailsEvent {
    object CloseScreenEvent : Event(), WordDetailsEvent
}
