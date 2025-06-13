package com.nikcapko.memo.presentation.screens.games.list

import com.nikcapko.domain.model.Game
import com.nikcapko.memo.core.ui.BaseEvent
import com.nikcapko.memo.core.ui.viewmodel.BaseViewModel
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.screens.games.list.state.GamesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GamesViewModel @Inject constructor(
    private var rootNavigator: RootNavigator,
) : BaseViewModel<GamesState, BaseEvent.Stub>() {

    override fun createInitialState() = GamesState(
        listOf(
            Game.SELECT_TRANSLATE,
            Game.FIND_PAIRS,
        )
    )

    fun onItemClick(game: Game) {
        when (game) {
            Game.SELECT_TRANSLATE -> rootNavigator.pushSelectTranslateScreen()
            Game.FIND_PAIRS -> rootNavigator.pushFindPairsScreen()
        }
    }

    fun onBackPressed() {
        rootNavigator.back()
    }
}
