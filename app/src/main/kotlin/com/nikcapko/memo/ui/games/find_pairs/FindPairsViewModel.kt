@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.find_pairs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.memo.data.Game
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FindPairsViewModel @Inject constructor(
    private val router: Router,
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val wordModelMapper: WordModelMapper,
) : ViewModel() {

    private val _state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: Flow<DataLoadingViewModelState> = _state.asStateFlow()

    private var words = emptyList<Word>()
    private var wordsCount = 0

    private val _findPairResultChannel = MutableStateFlow<Boolean?>(null)
    val findPairResultChannel = _findPairResultChannel.asStateFlow()

    private val _endGameChannel = MutableStateFlow<Unit?>(null)
    val endGameChannel = _endGameChannel.asStateFlow()

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { DataLoadingViewModelState.LoadingState }
            words = wordModelMapper.mapFromEntityList(
                gameWordsLimitUseCase(Game.MAX_WORDS_COUNT_FIND_PAIRS)
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
                _findPairResultChannel.update { true }
                _findPairResultChannel.update { null }
                wordsCount++
                if (wordsCount == Game.MAX_WORDS_COUNT_FIND_PAIRS * 2) {
                    _endGameChannel.update { Unit }
                }
            }
        }
        _findPairResultChannel.update { false }
        _findPairResultChannel.update { null }
    }

    fun onBackPressed() {
        router.exit()
    }
}
