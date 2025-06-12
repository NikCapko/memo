package com.nikcapko.memo.presentation.games.list

import com.nikcapko.domain.model.Game
import com.nikcapko.memo.core.ui.BaseState

internal data class GamesState(
    val games: List<Game>,
) : BaseState
