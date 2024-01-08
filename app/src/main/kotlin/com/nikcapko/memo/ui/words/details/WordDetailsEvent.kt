package com.nikcapko.memo.ui.words.details

import com.nikcapko.memo.base.ui.BaseEvent

internal interface WordDetailsEvent : BaseEvent {

    fun apply(eventController: WordDetailsEventController)

    data object CloseScreenEvent : WordDetailsEvent {
        override fun apply(eventController: WordDetailsEventController) {
            eventController.sendSuccessResult()
        }
    }
}
