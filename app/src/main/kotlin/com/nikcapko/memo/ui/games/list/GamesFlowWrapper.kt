package com.nikcapko.memo.ui.games.list

import com.nikcapko.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GamesFlowWrapper {
    val state: Flow<List<Game>>
}