package com.nik.capko.memo.ui.games.list

import com.nik.capko.memo.data.Game
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface GamesMvpView : MvpView {
    fun showGames(games: List<Game>)
}
