@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.find_pairs

import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.base.ui.BaseEventViewModel
import com.nikcapko.memo.base.ui.EventFlowWrapper
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.FindPairsInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MAX_WORDS_COUNT_FIND_PAIRS = 5

@HiltViewModel
internal class FindPairsViewModel @Inject constructor(
    private val findPairsInteractor: FindPairsInteractor,
    private val stateFlowWrapper: FindPairsStateFlowWrapper,
    eventFlowWrapper: EventFlowWrapper<FindPairsEvent>,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseEventViewModel<FindPairsEvent>(eventFlowWrapper, dispatcherProvider), FindPairsViewController {

    val state = stateFlowWrapper.liveValue()

    init {
        stateFlowWrapper.update(createInitialState())
        loadWords()
    }

    override fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            stateFlowWrapper.update { it.copy(dataLoadingViewModelState = DataLoadingViewModelState.LoadingState) }
            val words = findPairsInteractor.getWordsForGame()
            val wordList = words
                .map { it.word }
                .shuffled()
            val translateList = words
                .map { it.translation }
                .shuffled()
            stateFlowWrapper.update {
                it.copy(
                    dataLoadingViewModelState = DataLoadingViewModelState.LoadedState(words),
                    wordList = wordList,
                    translateList = translateList,
                )
            }
        }
    }

    override fun onFindPair(selectedWord: String, selectedTranslate: String) {
        val words =
            (stateFlowWrapper.value().dataLoadingViewModelState as? DataLoadingViewModelState.LoadedState<List<Word>>)?.data.orEmpty()
        var wordsCount = stateFlowWrapper.value().wordsCount
        words.forEach {
            if (it.word == selectedWord && it.translation == selectedTranslate) {
                wordsCount++
                if (wordsCount == MAX_WORDS_COUNT_FIND_PAIRS * 2) {
                    sendEvent(FindPairsEvent.EndGameEvent)
                } else {
                    sendEvent(FindPairsEvent.FindPairResultEvent(true))
                }
                stateFlowWrapper.update { it.copy(wordsCount = wordsCount) }
                return
            }
        }
        sendEvent(FindPairsEvent.FindPairResultEvent(false))
    }

    override fun onBackPressed() {
        navigator.back()
    }

    private fun createInitialState(): FindPairsState {
        return FindPairsState(
            dataLoadingViewModelState = DataLoadingViewModelState.NoneState,
            wordList = emptyList(),
            translateList = emptyList(),
            wordsCount = 0,
        )
    }
}
