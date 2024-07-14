package com.nikcapko.memo.presentation.words.list

import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.ui.viewmodel.BaseEventViewModel
import com.nikcapko.memo.presentation.domain.WordListInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_WORDS_COUNT = 5

@HiltViewModel
internal class WordListViewModel @Inject constructor(
    private val wordListInteractor: WordListInteractor,
    private val stateFlowWrapper: WordListStateFlowWrapper,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseEventViewModel<WordListEvent>(dispatcherProvider) {

    val state = stateFlowWrapper.liveValue()

    override fun onViewFirstCreated() {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            stateFlowWrapper.update(DataLoadingViewModelState.LoadingState)
            val wordsList = wordListInteractor.getWords()
            stateFlowWrapper.update(DataLoadingViewModelState.LoadedState(wordsList))
        }
    }

    fun onItemClick(position: Int) {
        val wordList =
            (stateFlowWrapper.value() as? DataLoadingViewModelState.LoadedState<List<Word>>)?.data
        rootNavigator.pushWordDetailScreen(wordList?.getOrNull(position))
    }

    fun onEnableSound(position: Int) {
        val wordList =
            (stateFlowWrapper.value() as? DataLoadingViewModelState.LoadedState<List<Word>>)?.data
        sendEvent(WordListEvent.SpeakOutEvent(wordList?.getOrNull(position)?.word.orEmpty()))
    }

    fun clearDatabase() {
        viewModelScope.launch(dispatcherProvider.io) {
            stateFlowWrapper.update(DataLoadingViewModelState.LoadingState)
            wordListInteractor.clearDataBase()
            stateFlowWrapper.update(DataLoadingViewModelState.LoadedState(emptyList<Word>()))
        }
    }

    fun onAddWordClick() {
        rootNavigator.pushWordDetailScreen()
    }

    fun openGamesScreen() {
        val wordsList =
            (stateFlowWrapper.value() as? DataLoadingViewModelState.LoadedState<List<Word>>)?.data
        if ((wordsList?.size ?: 0) < MIN_WORDS_COUNT) {
            sendEvent(WordListEvent.ShowNeedMoreWordsEvent)
        } else {
            rootNavigator.pushGamesScreen()
        }
    }

    fun onClearDatabaseClick() {
        sendEvent(WordListEvent.ShowClearDatabaseEvent)
    }
}
