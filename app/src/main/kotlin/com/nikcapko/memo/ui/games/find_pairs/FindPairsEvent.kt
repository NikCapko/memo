package com.nikcapko.memo.ui.games.find_pairs

import com.nikcapko.memo.base.ui.BaseEvent

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
