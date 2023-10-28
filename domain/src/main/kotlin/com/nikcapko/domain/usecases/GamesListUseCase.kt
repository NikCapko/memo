package com.nikcapko.domain.usecases

import com.nikcapko.domain.model.Game
import javax.inject.Inject

class GamesListUseCase @Inject constructor() {
    operator fun invoke(): List<Game> {
        return listOf(
            Game(Game.Type.SELECT_TRANSLATE, Game.Type.SELECT_TRANSLATE.value),
            Game(Game.Type.FIND_PAIRS, Game.Type.FIND_PAIRS.value),
        )
    }
}
