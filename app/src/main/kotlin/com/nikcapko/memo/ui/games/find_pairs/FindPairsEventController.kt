package com.nikcapko.memo.ui.games.find_pairs

internal interface FindPairsEventController {
    fun findPairResult(success: Boolean)
    fun endGame()
}