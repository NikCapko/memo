@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.find_pairs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.FindPairsInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MAX_WORDS_COUNT_FIND_PAIRS = 5

@HiltViewModel
internal class FindPairsViewModel @Inject constructor(
    private val findPairsInteractor: FindPairsInteractor,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: Flow<DataLoadingViewModelState> = _state.asStateFlow()

    private val _findPairResultEvent = MutableLiveData<FindPairsEvent.FindPairResultEvent>()
    val findPairResultEvent: LiveData<FindPairsEvent.FindPairResultEvent> = _findPairResultEvent

    private val _endGameEvent = MutableLiveData<FindPairsEvent.EndGameEvent>()
    val endGameEvent: LiveData<FindPairsEvent.EndGameEvent> = _endGameEvent

    private var words = emptyList<Word>()
    private var wordsCount = 0

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { DataLoadingViewModelState.LoadingState }
            words = findPairsInteractor.getWordsForGame()
            val wordList = words
                .map { it.word }
                .shuffled()
            val translateList = words
                .map { it.translation }
                .shuffled()
            _state.update { DataLoadingViewModelState.LoadedState(wordList to translateList) }
        }
    }

    fun onFindPair(selectedWord: String, selectedTranslate: String) {
        words.forEach {
            if (it.word == selectedWord && it.translation == selectedTranslate) {
                _findPairResultEvent.postValue(FindPairsEvent.FindPairResultEvent(true))
                wordsCount++
                if (wordsCount == MAX_WORDS_COUNT_FIND_PAIRS * 2) {
                    _endGameEvent.postValue(FindPairsEvent.EndGameEvent)
                }
                return
            }
        }
        _findPairResultEvent.postValue(FindPairsEvent.FindPairResultEvent(false))
    }

    fun onBackPressed() {
        navigator.back()
    }
}
