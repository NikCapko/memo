@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.select_translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class SelectTranslateViewModel @Inject constructor(
    private val router: Router,
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val saveWordUseCase: SaveWordUseCase,
    private val wordModelMapper: WordModelMapper,
) : ViewModel() {

    private val _state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: Flow<DataLoadingViewModelState> = _state.asStateFlow()

    private val _successAnimationChannel = MutableStateFlow<Boolean?>(null)
    val successAnimationChannel = _successAnimationChannel.asStateFlow()

    private val _endGameChannel = MutableStateFlow<Pair<Int, Int>?>(null)
    val endGameChannel = _endGameChannel.asStateFlow()

    private var words: List<Word>? = null
    private var word: Word? = null

    private var counter = 0
    private var errorCounter = 0
    private var successCounter = 0

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { DataLoadingViewModelState.LoadingState }
            words =
                wordModelMapper.mapFromEntityList(gameWordsLimitUseCase(Game.MAX_WORDS_COUNT_SELECT_TRANSLATE))
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
            word?.frequency = word?.frequency?.plus(Word.WORD_GAME_PRICE) ?: Word.WORD_GAME_PRICE
            _successAnimationChannel.update { null }
            _successAnimationChannel.update { true }
            successCounter++
        } else {
            word?.frequency = word?.frequency?.minus(Word.WORD_GAME_PRICE) ?: -Word.WORD_GAME_PRICE
            _successAnimationChannel.update { null }
            _successAnimationChannel.update { false }
            errorCounter++
        }
    }

    fun onAnimationEnd() {
        if (counter == Game.MAX_WORDS_COUNT_SELECT_TRANSLATE - 1) {
            updateWordsDB()
        } else {
            counter++
            updateWord()
        }
    }

    private fun updateWordsDB() {
        viewModelScope.launch(Dispatchers.IO) {
            words?.forEach { word ->
                saveWordUseCase(wordModelMapper.mapToEntity(word))
            }
            withContext(Dispatchers.Main) {
                _endGameChannel.update { successCounter to errorCounter }
            }
        }
    }

    fun onBackPressed() {
        router.exit()
    }
}
