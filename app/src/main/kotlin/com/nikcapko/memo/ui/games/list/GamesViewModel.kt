package com.nikcapko.memo.ui.games.list

import com.nikcapko.domain.model.Game
import com.nikcapko.memo.base.ui.BaseViewModel
import com.nikcapko.memo.domain.GamesInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GamesViewModel @Inject constructor(
    private var gamesInteractor: GamesInteractor,
    private var gamesFlowWrapper: GamesFlowWrapper,
    private var navigator: Navigator,
) : BaseViewModel(), GamesViewController {

    val state = gamesFlowWrapper.liveValue()

    init {
        loadGames()
    }

    private fun loadGames() {
        gamesFlowWrapper.update(gamesInteractor.getDefaultGamesList())
    }

    override fun onItemClick(position: Int) {
        when (gamesFlowWrapper.value().getOrNull(position)?.type) {
            Game.Type.SELECT_TRANSLATE -> navigator.pushSelectTranslateScreen()
            Game.Type.FIND_PAIRS -> navigator.pushFindPairsScreen()
            else -> Unit
        }
    }
}
