package com.nikcapko.memo.ui.games.find_pairs

sealed interface FindPairsEvent {
    data class FindPairResultEvent(val success: Boolean) : FindPairsEvent
    data object EndGameEvent : FindPairsEvent
}
