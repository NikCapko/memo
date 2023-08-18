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
import kotlinx.coroutines.channels.Channel
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

    private val mutableStateFlow =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: StateFlow<DataLoadingViewModelState>
        get() = mutableStateFlow.asStateFlow()

    val speakOutChannel = Channel<String>()

    private var wordsList = emptyList<Word>()

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch {
            mutableStateFlow.update { DataLoadingViewModelState.LoadingState }
            wordsList = wordModelMapper.mapFromEntityList(wordListUseCase())
            mutableStateFlow.update { DataLoadingViewModelState.LoadedState(wordsList) }
        }
    }

    fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        router.navigateTo(Screens.wordDetailScreen(word))
    }

    fun onEnableSound(position: Int) {
        val word = wordsList.getOrNull(position)
        viewModelScope.launch {
            speakOutChannel.send(word?.word.orEmpty())
        }
    }

    fun onAddWordClick() {
        router.navigateTo(Screens.wordDetailScreen(null))
    }

    fun openGamesScreen() {
        router.navigateTo(Screens.gamesScreen())
    }
}
