package com.nikcapko.memo.ui.games.list

import com.github.terrakok.cicerone.Router
import com.nikcapko.memo.data.Game
import com.nikcapko.memo.ui.Screens
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class GamesPresenter @Inject constructor(
    private var router: Router,
) : MvpPresenter<GamesMvpView>() {

    private var games = emptyList<Game>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadGames()
    }

    private fun loadGames() {
        games = Game.getDefaultList()
        viewState.showGames(games)
    }

    fun onItemClick(position: Int) {
        when (games.getOrNull(position)?.type) {
            Game.Type.SELECT_TRANSLATE -> router.navigateTo(Screens.selectTranslateScreen())
            Game.Type.FIND_PAIRS -> router.navigateTo(Screens.findPairsScreen())
            Game.Type.PHRASES -> router.navigateTo(Screens.phrasesScreen())
            else -> {}
        }
    }
}
