package com.nik.capko.memo.ui.games.list

import com.nik.capko.memo.data.Game
import com.nik.capko.memo.data.Word
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface GamesMvpView : MvpView {
    fun showGames(games: List<Game>)
    fun showGame(gameId: Game.Type, words: List<Word>)
}
