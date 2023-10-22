package com.nikcapko.memo.ui.games.find_pairs

import ru.ar2code.mutableliveevent.Event
import ru.ar2code.mutableliveevent.EventArgs

sealed interface FindPairsEvent {
    data class FindPairResultEvent(val success: Boolean) : EventArgs<Boolean>(success), FindPairsEvent
    object EndGameEvent : Event(), FindPairsEvent
}
