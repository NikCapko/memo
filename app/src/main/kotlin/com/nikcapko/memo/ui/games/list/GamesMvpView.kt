package com.nikcapko.memo.ui.games.list

import com.nikcapko.memo.data.Game
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface GamesMvpView : MvpView {
    fun showGames(games: List<Game>)
}
