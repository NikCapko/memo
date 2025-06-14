package com.nikcapko.memo.presentation.screens.games.list.state

import com.nikcapko.memo.domain.model.Game
import com.nikcapko.memo.core.ui.BaseState

internal data class GamesState(
    val games: List<Game>,
) : BaseState
