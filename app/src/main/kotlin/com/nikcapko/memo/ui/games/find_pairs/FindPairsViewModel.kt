@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.find_pairs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.MAX_WORDS_COUNT_FIND_PAIRS
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.ar2code.mutableliveevent.Event
import ru.ar2code.mutableliveevent.EventArgs
import javax.inject.Inject

@HiltViewModel
internal class FindPairsViewModel @Inject constructor(
    private val router: Router,
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val wordModelMapper: WordModelMapper,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: Flow<DataLoadingViewModelState> = _state.asStateFlow()

    private var words = emptyList<Word>()
    private var wordsCount = 0

    private val _findPairResultEvent = MutableLiveData<EventArgs<Boolean>>()
    val findPairResultEvent: LiveData<EventArgs<Boolean>> = _findPairResultEvent

    private val _endGameEvent = MutableLiveData<Event>()
    val endGameEvent: LiveData<Event> = _endGameEvent

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { DataLoadingViewModelState.LoadingState }
            words = wordModelMapper.mapFromEntityList(
                gameWordsLimitUseCase(MAX_WORDS_COUNT_FIND_PAIRS)
            )
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
                _findPairResultEvent.postValue(EventArgs(true))
                wordsCount++
                if (wordsCount == MAX_WORDS_COUNT_FIND_PAIRS * 2) {
                    _endGameEvent.postValue(Event())
                }
            }
        }
        _findPairResultEvent.postValue(EventArgs(false))
    }

    fun onBackPressed() {
        router.exit()
    }
}
