package com.nikcapko.memo.presentation.games.list

import com.nikcapko.domain.model.Game
import com.nikcapko.memo.core.ui.viewmodel.LazyViewModel
import com.nikcapko.memo.presentation.domain.GamesInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GamesViewModel @Inject constructor(
    private var gamesInteractor: GamesInteractor,
    private var gamesFlowWrapper: GamesFlowWrapper,
    private var rootNavigator: RootNavigator,
) : LazyViewModel(), GamesCommandReceiver {

    val state = gamesFlowWrapper.liveValue()

    override fun onViewFirstCreated() {
        loadGames()
    }

    private fun loadGames() {
        gamesFlowWrapper.update(gamesInteractor.getDefaultGamesList())
    }

    override fun onItemClick(position: Int) {
        when (gamesFlowWrapper.value().getOrNull(position)?.type) {
            Game.Type.SELECT_TRANSLATE -> rootNavigator.pushSelectTranslateScreen()
            Game.Type.FIND_PAIRS -> rootNavigator.pushFindPairsScreen()
            else -> Unit
        }
    }
}
