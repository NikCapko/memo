package com.nikcapko.memo.ui.games.list

import androidx.lifecycle.ViewModel
import com.nikcapko.domain.model.Game
import com.nikcapko.memo.domain.GamesInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class GamesViewModel @Inject constructor(
    private var gamesInteractor: GamesInteractor,
    private var navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow<List<Game>>(emptyList())
    val state: Flow<List<Game>> = _state.asStateFlow()

    init {
        loadGames()
    }

    private fun loadGames() {
        _state.update { gamesInteractor.getDefaultGamesList() }
    }

    fun onItemClick(position: Int) {
        when (_state.value.getOrNull(position)?.type) {
            Game.Type.SELECT_TRANSLATE -> navigator.pushSelectTranslateScreen()
            Game.Type.FIND_PAIRS -> navigator.pushFindPairsScreen()
            else -> Unit
        }
    }
}
