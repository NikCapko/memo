package com.nikcapko.memo.presentation.screens.games.findpairs

import androidx.lifecycle.viewModelScope
import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.common.exceptionHandler
import com.nikcapko.memo.core.ui.viewmodel.BaseViewModel
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.screens.games.findpairs.event.FindPairsEvent
import com.nikcapko.memo.presentation.screens.games.findpairs.state.FindPairsState
import com.nikcapko.memo.presentation.screens.games.findpairs.state.mapSuccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val MAX_WORDS_COUNT_FIND_PAIRS = 5

@HiltViewModel
internal class FindPairsViewModel @Inject constructor(
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<FindPairsState, FindPairsEvent>() {

    override fun createInitialState(): FindPairsState {
        return FindPairsState.None
    }

    override fun onViewFirstCreated() {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(
            exceptionHandler {
                updateState { FindPairsState.Error("Произошла ошибка") }
            },
        ) {
            withContext(dispatcherProvider.io) {
                updateState { FindPairsState.Loading }
                val words = gameWordsLimitUseCase(MAX_WORDS_COUNT_FIND_PAIRS)
                val wordList = words
                    .map {
                        FindPairsState.Item(
                            id = it.id.toString(),
                            value = it.word,
                            isVisible = true,
                        )
                    }
                    .shuffled()
                val translateList = words
                    .map {
                        FindPairsState.Item(
                            id = it.id.toString(),
                            value = it.translate,
                            isVisible = true,
                        )
                    }
                    .shuffled()
                updateState { FindPairsState.Success(wordList, translateList) }
            }
        }
    }

    fun onWordClick(word: FindPairsState.Item) {
        var needCheckEndGame = false
        updateState {
            it.mapSuccessState { state ->
                when {
                    state.selectWord != null -> {
                        state.copy(
                            wordList = state.wordList.map { originalWord ->
                                when (originalWord.id) {
                                    word.id -> originalWord.copy(checked = true)
                                    state.selectWord.id -> originalWord.copy(checked = false)
                                    else -> originalWord
                                }
                            },
                            selectWord = word,
                        )
                    }

                    state.selectTranslate != null -> {
                        if (state.selectTranslate.id == word.id) {
                            needCheckEndGame = true
                            state.copy(
                                wordList = state.wordList.map { originalWord ->
                                    if (originalWord.id == word.id) {
                                        originalWord.copy(isVisible = false)
                                    } else {
                                        originalWord
                                    }
                                },
                                translateList = state.translateList.map { originalTranslate ->
                                    if (originalTranslate.id == word.id) {
                                        originalTranslate.copy(isVisible = false)
                                    } else {
                                        originalTranslate
                                    }
                                },
                                selectWord = null,
                                selectTranslate = null,
                            )
                        } else {
                            state.copy(
                                wordList = state.wordList.map { originalWord ->
                                    if (originalWord.id == word.id) {
                                        originalWord.copy(checked = true)
                                    } else {
                                        originalWord
                                    }
                                },
                                translateList = state.translateList.map { it.copy(checked = false) },
                                selectWord = word,
                                selectTranslate = null,
                            )
                        }
                    }

                    else -> {
                        state.copy(
                            wordList = state.wordList.map { originalWord ->
                                if (originalWord.id == word.id) {
                                    originalWord.copy(checked = true)
                                } else {
                                    originalWord
                                }
                            },
                            selectWord = word,
                        )
                    }
                }
            }
        }
        if (needCheckEndGame) {
            checkEndGame()
        }
    }

    fun onTranslateClick(translate: FindPairsState.Item) {
        var needCheckEndGame = false
        updateState {
            it.mapSuccessState { state ->
                when {
                    state.selectTranslate != null -> {
                        state.copy(
                            wordList = state.wordList.map { originalTranslate ->
                                when (originalTranslate.id) {
                                    translate.id -> originalTranslate.copy(checked = true)
                                    state.selectTranslate.id -> originalTranslate.copy(checked = false)
                                    else -> originalTranslate
                                }
                            },
                            selectTranslate = translate,
                        )
                    }

                    state.selectWord != null -> {
                        if (state.selectWord.id == translate.id) {
                            needCheckEndGame = true
                            state.copy(
                                wordList = state.wordList.map { originalWord ->
                                    if (originalWord.id == translate.id) {
                                        originalWord.copy(isVisible = false)
                                    } else {
                                        originalWord
                                    }
                                },
                                translateList = state.translateList.map { originalTranslate ->
                                    if (originalTranslate.id == translate.id) {
                                        originalTranslate.copy(isVisible = false)
                                    } else {
                                        originalTranslate
                                    }
                                },
                                selectWord = null,
                                selectTranslate = null,
                            )
                        } else {
                            state.copy(
                                wordList = state.wordList.map { it.copy(checked = false) },
                                translateList = state.translateList.map { originalTranslate ->
                                    if (originalTranslate.id == translate.id) {
                                        originalTranslate.copy(checked = true)
                                    } else {
                                        originalTranslate
                                    }
                                },
                                selectWord = null,
                                selectTranslate = translate,
                            )
                        }
                    }

                    else -> {
                        state.copy(
                            translateList = state.translateList.map { originalTranslate ->
                                if (originalTranslate.id == translate.id) {
                                    originalTranslate.copy(checked = true)
                                } else {
                                    originalTranslate
                                }
                            },
                            selectTranslate = translate,
                        )
                    }
                }
            }
        }
        if (needCheckEndGame) {
            checkEndGame()
        }
    }

    private fun checkEndGame() {
        if ((state.value as FindPairsState.Success).wordList.all { !it.isVisible }) {
            updateState { FindPairsState.EndGame }
        }
    }

    fun onBackPressed() {
        rootNavigator.back()
    }
}
