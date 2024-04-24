package com.nikcapko.memo.presentation.games.findpairs

import com.nikcapko.memo.core.ui.BaseEvent

internal interface FindPairsEvent : BaseEvent {

    fun apply(eventController: FindPairsEventController)

    data class FindPairResultEvent(val success: Boolean) : FindPairsEvent {
        override fun apply(eventController: FindPairsEventController) {
            eventController.findPairResult(success)
        }
    }

    data object EndGameEvent : FindPairsEvent {
        override fun apply(eventController: FindPairsEventController) {
            eventController.endGame()
        }
    }
}
