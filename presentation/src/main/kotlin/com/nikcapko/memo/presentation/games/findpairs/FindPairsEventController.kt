package com.nikcapko.memo.presentation.games.findpairs

internal interface FindPairsEventController {
    fun findPairResult(success: Boolean)
    fun endGame()
}