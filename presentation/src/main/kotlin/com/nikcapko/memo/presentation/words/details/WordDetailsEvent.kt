package com.nikcapko.memo.presentation.words.details

import com.nikcapko.memo.core.ui.BaseEvent

internal interface WordDetailsEvent : BaseEvent {

    fun apply(eventController: WordDetailsEventController)

    data object CloseScreenEvent : WordDetailsEvent {
        override fun apply(eventController: WordDetailsEventController) {
            eventController.sendSuccessResult()
        }
    }
}
