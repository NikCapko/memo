package com.nikcapko.memo.ui.games.select_translate

internal sealed interface SelectTranslateEvent {
    data class SuccessAnimationEvent(val success: Boolean) : SelectTranslateEvent
    data class EndGameEvent(val successCount: Int, val errorCount: Int) : SelectTranslateEvent
}
