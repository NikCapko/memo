package com.nikcapko.memo.ui.games.select_translate

import ru.ar2code.mutableliveevent.EventArgs

internal sealed interface SelectTranslateEvent {

    data class SuccessAnimationEvent(val success: Boolean) :
        EventArgs<Boolean>(success),
        SelectTranslateEvent

    data class EndGameEvent(
        val successCount: Int,
        val errorCount: Int,
    ) : EventArgs<Pair<Int, Int>>(successCount to errorCount), SelectTranslateEvent
}
