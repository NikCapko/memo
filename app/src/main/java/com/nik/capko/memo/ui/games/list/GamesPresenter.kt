package com.nik.capko.memo.ui.games.list

import com.nik.capko.memo.data.Game
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.repository.WordRepository
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class GamesPresenter @Inject constructor(
    private val wordRepository: WordRepository
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
        games.getOrNull(position)?.let {
            viewState.showGame(it.type, listOf<Word>())
        }
    }
}
