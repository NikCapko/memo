package com.nikcapko.memo.ui.games.findpairs

internal interface FindPairsEventController {
    fun findPairResult(success: Boolean)
    fun endGame()
}