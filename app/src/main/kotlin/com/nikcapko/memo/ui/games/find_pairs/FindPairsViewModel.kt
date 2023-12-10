@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.find_pairs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.FindPairsInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MAX_WORDS_COUNT_FIND_PAIRS = 5

@HiltViewModel
internal class FindPairsViewModel @Inject constructor(
    private val findPairsInteractor: FindPairsInteractor,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(), FindPairsFlowWrapper, FindPairsViewController {

    override val state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    override val eventFlow = MutableSharedFlow<FindPairsEvent>()

    private var words = emptyList<Word>()
    private var wordsCount = 0

    init {
        loadWords()
    }

    override fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            state.update { DataLoadingViewModelState.LoadingState }
            words = findPairsInteractor.getWordsForGame()
            val wordList = words
                .map { it.word }
                .shuffled()
            val translateList = words
                .map { it.translation }
                .shuffled()
            state.update { DataLoadingViewModelState.LoadedState(wordList to translateList) }
        }
    }

    override fun onFindPair(selectedWord: String, selectedTranslate: String) {
        words.forEach {
            if (it.word == selectedWord && it.translation == selectedTranslate) {
                viewModelScope.launch {
                    eventFlow.emit(FindPairsEvent.FindPairResultEvent(true))
                }
                wordsCount++
                if (wordsCount == MAX_WORDS_COUNT_FIND_PAIRS * 2) {
                    viewModelScope.launch { eventFlow.emit(FindPairsEvent.EndGameEvent) }
                }
                return
            }
        }
        viewModelScope.launch { eventFlow.emit(FindPairsEvent.FindPairResultEvent(false)) }
    }

    override fun onBackPressed() {
        navigator.back()
    }
}
