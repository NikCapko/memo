package com.nikcapko.memo.ui.words.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.WordListInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_WORDS_COUNT = 5

@HiltViewModel
internal class WordListViewModel @Inject constructor(
    private val wordListInteractor: WordListInteractor,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(), WordListFlowWrapper, WordListViewController {

    override val state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    override val event = MutableSharedFlow<WordListEvent>()

    private var wordsList = emptyList<Word>()

    init {
        loadWords()
    }

    override fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            state.update { DataLoadingViewModelState.LoadingState }
            wordsList = wordListInteractor.getWords()
            state.update { DataLoadingViewModelState.LoadedState(wordsList) }
        }
    }

    override fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        navigator.pushWordDetailScreen(word)
    }

    override fun onEnableSound(position: Int) {
        val word = wordsList.getOrNull(position)
        viewModelScope.launch { event.emit(WordListEvent.SpeakOutEvent(word?.word.orEmpty())) }
    }

    override fun clearDatabase() {
        viewModelScope.launch(dispatcherProvider.io) {
            state.update { DataLoadingViewModelState.LoadingState }
            wordListInteractor.clearDataBase()
            state.update { DataLoadingViewModelState.LoadedState(emptyList<Word>()) }
        }
    }

    override fun onAddWordClick() {
        navigator.pushWordDetailScreen()
    }

    override fun openGamesScreen() {
        if (wordsList.size < MIN_WORDS_COUNT) {
            viewModelScope.launch { event.tryEmit(WordListEvent.ShowNeedMoreWordsEvent) }
        } else {
            navigator.pushGamesScreen()
        }
    }

    override fun onClearDatabaseClick() {
        viewModelScope.launch { event.tryEmit(WordListEvent.ShowClearDatabaseEvent) }
    }
}
