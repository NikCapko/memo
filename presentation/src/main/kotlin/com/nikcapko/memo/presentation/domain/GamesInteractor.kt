package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.model.Game
import com.nikcapko.domain.usecases.GamesListUseCase
import javax.inject.Inject

internal interface GamesInteractor {
    fun getDefaultGamesList(): List<Game>
}

internal class GamesInteractorImpl @Inject constructor(
    private val gamesListUseCase: GamesListUseCase,
) : GamesInteractor {

    override fun getDefaultGamesList(): List<Game> {
        return gamesListUseCase()
    }
}
