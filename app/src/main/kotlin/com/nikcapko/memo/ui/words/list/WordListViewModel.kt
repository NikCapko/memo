package com.nikcapko.memo.ui.words.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.WordListInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_WORDS_COUNT = 5

@HiltViewModel
internal class WordListViewModel @Inject constructor(
    private val wordListInteractor: WordListInteractor,
    private val stateFlowWrapper: WordListStateFlowWrapper,
    private val eventFlowWrapper: WordListEventFlowWrapper,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(), WordListViewController {

    val state = stateFlowWrapper.liveValue()
    val eventFlow = eventFlowWrapper.liveValue()

    init {
        loadWords()
    }

    override fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            stateFlowWrapper.update(DataLoadingViewModelState.LoadingState)
            val wordsList = wordListInteractor.getWords()
            stateFlowWrapper.update(DataLoadingViewModelState.LoadedState(wordsList))
        }
    }

    override fun onItemClick(position: Int) {
        val wordList =
            (stateFlowWrapper.value() as? DataLoadingViewModelState.LoadedState<List<Word>>)?.data
        navigator.pushWordDetailScreen(wordList?.getOrNull(position))
    }

    override fun onEnableSound(position: Int) {
        val wordList =
            (stateFlowWrapper.value() as? DataLoadingViewModelState.LoadedState<List<Word>>)?.data
        viewModelScope.launch(dispatcherProvider.main) {
            eventFlowWrapper.update(WordListEvent.SpeakOutEvent(wordList?.getOrNull(position)?.word.orEmpty()))
        }
    }

    override fun clearDatabase() {
        viewModelScope.launch(dispatcherProvider.io) {
            stateFlowWrapper.update(DataLoadingViewModelState.LoadingState)
            wordListInteractor.clearDataBase()
            stateFlowWrapper.update(DataLoadingViewModelState.LoadedState(emptyList<Word>()))
        }
    }

    override fun onAddWordClick() {
        navigator.pushWordDetailScreen()
    }

    override fun openGamesScreen() {
        val wordsList =
            (stateFlowWrapper.value() as? DataLoadingViewModelState.LoadedState<List<Word>>)?.data
        if ((wordsList?.size ?: 0) < MIN_WORDS_COUNT) {
            viewModelScope.launch(dispatcherProvider.main) {
                eventFlowWrapper.update(WordListEvent.ShowNeedMoreWordsEvent)
            }
        } else {
            navigator.pushGamesScreen()
        }
    }

    override fun onClearDatabaseClick() {
        viewModelScope.launch(dispatcherProvider.main) {
            eventFlowWrapper.update(WordListEvent.ShowClearDatabaseEvent)
        }
    }
}
