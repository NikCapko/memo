package com.nikcapko.memo.ui.games.list

import androidx.lifecycle.ViewModel
import com.nikcapko.domain.model.Game
import com.nikcapko.memo.domain.GamesInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class GamesViewModel @Inject constructor(
    private var gamesInteractor: GamesInteractor,
    private var navigator: Navigator,
) : ViewModel(), GamesFlowWrapper, GamesViewController {

    override val state = MutableStateFlow<List<Game>>(emptyList())

    init {
        loadGames()
    }

    private fun loadGames() {
        state.update { gamesInteractor.getDefaultGamesList() }
    }

    override fun onItemClick(position: Int) {
        when (state.value.getOrNull(position)?.type) {
            Game.Type.SELECT_TRANSLATE -> navigator.pushSelectTranslateScreen()
            Game.Type.FIND_PAIRS -> navigator.pushFindPairsScreen()
            else -> Unit
        }
    }
}
