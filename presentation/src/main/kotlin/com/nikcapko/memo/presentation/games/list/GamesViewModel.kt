package com.nikcapko.memo.presentation.games.list

import com.nikcapko.domain.model.Game
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.core.ui.viewmodel.BaseViewModel
import com.nikcapko.memo.presentation.domain.GamesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GamesViewModel @Inject constructor(
    private var gamesInteractor: GamesInteractor,
    private var gamesFlowWrapper: GamesFlowWrapper,
    private var rootNavigator: RootNavigator,
) : BaseViewModel(), GamesCommandReceiver {

    val state = gamesFlowWrapper.liveValue()

    init {
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
