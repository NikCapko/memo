package com.nikcapko.memo.presentation.screens.games.findpairs.event

import com.nikcapko.memo.core.ui.BaseEvent

internal interface FindPairsEvent : BaseEvent {
    data class FindPairResultEvent(val success: Boolean) : FindPairsEvent
    data object EndGameEvent : FindPairsEvent
}
