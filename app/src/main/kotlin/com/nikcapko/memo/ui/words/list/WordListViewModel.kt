package com.nikcapko.memo.ui.words.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import com.nikcapko.memo.ui.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WordListViewModel @Inject constructor(
    private val router: Router,
    private val wordListUseCase: WordListUseCase,
    private val wordModelMapper: WordModelMapper,
) : ViewModel() {

    private val _state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: Flow<DataLoadingViewModelState>
        get() = _state.asStateFlow()

    private val _speakOutChannel = MutableStateFlow("")
    val speakOutChannel: StateFlow<String>
        get() = _speakOutChannel.asStateFlow()

    private var wordsList = emptyList<Word>()

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch {
            _state.update { DataLoadingViewModelState.LoadingState }
            val wordModelList = wordListUseCase()
            wordsList = wordModelMapper.mapFromEntityList(wordModelList)
            _state.update { DataLoadingViewModelState.LoadedState(wordsList) }
        }
    }

    fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        router.navigateTo(Screens.wordDetailScreen(word))
    }

    fun onEnableSound(position: Int) {
        val word = wordsList.getOrNull(position)
        _speakOutChannel.update { word?.word.orEmpty() }
    }

    fun onAddWordClick() {
        router.navigateTo(Screens.wordDetailScreen(null))
    }

    fun openGamesScreen() {
        router.navigateTo(Screens.gamesScreen())
    }
}
