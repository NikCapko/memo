package com.nikcapko.memo.ui.games.list

import com.nikcapko.domain.model.Game
import com.nikcapko.memo.base.ui.flow.StateFlowWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class GamesFlowWrapper @Inject constructor() : StateFlowWrapper<List<Game>> {

    private val state = MutableStateFlow<List<Game>?>(null)

    override fun value(): List<Game> = requireNotNull(state.value)

    override fun liveValue(): Flow<List<Game>> = state.filterNotNull().distinctUntilChanged()

    override fun update(value: List<Game>) {
        state.update { value }
    }
}
