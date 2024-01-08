package com.nikcapko.memo.ui.games.select_translate

internal interface SelectTranslateEventController {
    fun showSuccessAnimation()
    fun showErrorAnimation()
    fun showEndGame(successCount: Int, errorCount: Int)
}
