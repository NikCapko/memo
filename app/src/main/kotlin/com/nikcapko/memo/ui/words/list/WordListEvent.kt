package com.nikcapko.memo.ui.words.list

import com.nikcapko.memo.base.ui.BaseEvent

internal interface WordListEvent : BaseEvent {

    fun apply(eventController: WordListEventController)

    data class SpeakOutEvent(private val word: String) : WordListEvent {
        override fun apply(eventController: WordListEventController) {
            eventController.speakOut(word)
        }
    }

    data object ShowClearDatabaseEvent : WordListEvent {
        override fun apply(eventController: WordListEventController) {
            eventController.showClearDatabaseDialog()
        }
    }

    data object ShowNeedMoreWordsEvent : WordListEvent {
        override fun apply(eventController: WordListEventController) {
            eventController.showNeedMoreWordsDialog()
        }
    }
}
