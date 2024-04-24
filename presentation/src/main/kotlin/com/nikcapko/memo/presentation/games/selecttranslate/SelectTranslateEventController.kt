package com.nikcapko.memo.presentation.games.selecttranslate

internal interface SelectTranslateEventController {
    fun showSuccessAnimation()
    fun showErrorAnimation()
    fun showEndGame(successCount: Int, errorCount: Int)
}
