@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.select_translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.WORD_GAME_PRICE
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.MAX_WORDS_COUNT_SELECT_TRANSLATE
import com.nikcapko.memo.domain.SelectTranslateInteractor
import com.nikcapko.memo.navigation.Navigator
import com.nikcapko.memo.ui.games.find_pairs.FindPairsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectTranslateViewModel @Inject constructor(
    private val selectTranslateInteractor: SelectTranslateInteractor,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: Flow<DataLoadingViewModelState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<SelectTranslateEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var words: List<Word>? = null
    private var word: Word? = null

    private var counter = 0
    private var errorCounter = 0
    private var successCounter = 0

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { DataLoadingViewModelState.LoadingState }
            words = selectTranslateInteractor.getWords()
            updateWord()
        }
    }

    private fun updateWord() {
        word = words?.getOrNull(counter)
        val translates = words?.toMutableList()
            ?.shuffled()
            ?.map { it.translation }
            .orEmpty()
        _state.update { DataLoadingViewModelState.LoadedState(word to translates) }
    }

    fun onTranslateClick(translate: String) {
        if (word?.translation.equals(translate)) {
            word?.frequency = word?.frequency?.plus(WORD_GAME_PRICE) ?: WORD_GAME_PRICE
            viewModelScope.launch {
                _eventFlow.emit(SelectTranslateEvent.SuccessAnimationEvent(true))
            }
            successCounter++
        } else {
            word?.frequency = word?.frequency?.minus(WORD_GAME_PRICE) ?: -WORD_GAME_PRICE
            viewModelScope.launch {
                _eventFlow.emit(SelectTranslateEvent.SuccessAnimationEvent(false))
            }
            errorCounter++
        }
    }

    fun onAnimationEnd() {
        if (counter == MAX_WORDS_COUNT_SELECT_TRANSLATE - 1) {
            updateWordsDB()
        } else {
            counter++
            updateWord()
        }
    }

    private fun updateWordsDB() {
        viewModelScope.launch(dispatcherProvider.io) {
            words?.forEach { word ->
                selectTranslateInteractor.saveWord(word)
            }
            viewModelScope.launch {
                _eventFlow.emit(SelectTranslateEvent.EndGameEvent(successCounter, errorCounter))
            }
        }
    }

    fun onBackPressed() {
        navigator.back()
    }
}
