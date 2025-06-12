package com.nikcapko.memo.presentation.screens.games.findpairs

import androidx.lifecycle.viewModelScope
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.common.emptyExceptionHandler
import com.nikcapko.memo.core.ui.viewmodel.BaseViewModel
import com.nikcapko.memo.presentation.domain.FindPairsInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.screens.games.findpairs.event.FindPairsEvent
import com.nikcapko.memo.presentation.screens.games.findpairs.state.FindPairsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val MAX_WORDS_COUNT_FIND_PAIRS = 5

@HiltViewModel
internal class FindPairsViewModel @Inject constructor(
    private val findPairsInteractor: FindPairsInteractor,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<FindPairsState, FindPairsEvent>() {

    override fun createInitialState(): FindPairsState {
        return FindPairsState(
            dataLoadingViewModelState = DataLoadingViewModelState.NoneState,
            wordList = emptyList(),
            translateList = emptyList(),
            wordsCount = 0,
        )
    }

    override fun onViewFirstCreated() {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(emptyExceptionHandler) {
            withContext(dispatcherProvider.io) {
                updateState { it.copy(dataLoadingViewModelState = DataLoadingViewModelState.LoadingState) }
                val words = findPairsInteractor.getWordsForGame()
                val wordList = words
                    .map { it.word }
                    .shuffled()
                val translateList = words
                    .map { it.translate }
                    .shuffled()
                updateState {
                    it.copy(
                        dataLoadingViewModelState = DataLoadingViewModelState.LoadedState(words),
                        wordList = wordList,
                        translateList = translateList,
                    )
                }
            }
        }
    }

    fun onFindPair(selectedWord: String, selectedTranslate: String) {
        val words =
            (state.value.dataLoadingViewModelState as? DataLoadingViewModelState.LoadedState<List<WordModel>>)?.data.orEmpty()
        var wordsCount = state.value.wordsCount
        words.forEach {
            if (it.word == selectedWord && it.translate == selectedTranslate) {
                wordsCount++
                if (wordsCount == MAX_WORDS_COUNT_FIND_PAIRS * 2) {
                    sendEvent(FindPairsEvent.EndGameEvent)
                } else {
                    sendEvent(FindPairsEvent.FindPairResultEvent(true))
                }
                updateState { it.copy(wordsCount = wordsCount) }
                return
            }
        }
        sendEvent(FindPairsEvent.FindPairResultEvent(false))
    }

    fun onBackPressed() {
        rootNavigator.back()
    }
}
