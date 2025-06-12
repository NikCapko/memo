package com.nikcapko.memo.presentation.games.selecttranslate

import com.nikcapko.memo.core.ui.BaseEvent

internal sealed interface SelectTranslateEvent : BaseEvent {
    data object SuccessAnimationEvent : SelectTranslateEvent
    data object ErrorAnimationEvent : SelectTranslateEvent
    data class EndGameEvent(
        val successCount: Int,
        val errorCount: Int,
    ) : SelectTranslateEvent
}
