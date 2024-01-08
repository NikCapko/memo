package com.nikcapko.memo.ui.games.select_translate

import com.nikcapko.memo.base.ui.BaseEvent

internal interface SelectTranslateEvent : BaseEvent {

    fun apply(eventController: SelectTranslateEventController)

    data object SuccessAnimationEvent : SelectTranslateEvent {
        override fun apply(eventController: SelectTranslateEventController) {
            eventController.showSuccessAnimation()
        }
    }

    data object ErrorAnimationEvent : SelectTranslateEvent {
        override fun apply(eventController: SelectTranslateEventController) {
            eventController.showErrorAnimation()
        }
    }

    data class EndGameEvent(
        private val successCount: Int,
        private val errorCount: Int,
    ) : SelectTranslateEvent {
        override fun apply(eventController: SelectTranslateEventController) {
            eventController.showEndGame(successCount, errorCount)
        }
    }
}