@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.presentation.screens.games.selecttranslate

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.common.exceptionHandler
import com.nikcapko.memo.core.ui.BaseEvent
import com.nikcapko.memo.core.ui.viewmodel.BaseViewModel
import com.nikcapko.memo.presentation.domain.MAX_WORDS_COUNT_SELECT_TRANSLATE
import com.nikcapko.memo.presentation.domain.SelectTranslateInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.screens.games.selecttranslate.state.SelectTranslateScreenState
import com.nikcapko.memo.presentation.screens.games.selecttranslate.state.SelectTranslateState
import com.nikcapko.memo.presentation.screens.games.selecttranslate.state.mapSuccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal const val WORD_GAME_PRICE = 0.02f

@HiltViewModel
internal class SelectTranslateViewModel @Inject constructor(
    private val selectTranslateInteractor: SelectTranslateInteractor,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<SelectTranslateState, BaseEvent.Stub>() {

    override fun createInitialState() = SelectTranslateState.None

    override fun onViewFirstCreated() {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(
            exceptionHandler {
                updateState { SelectTranslateState.Error("Произошла ошибка") }
            }
        ) {
            withContext(dispatcherProvider.io) {
                updateState { SelectTranslateState.Loading }
                val words = selectTranslateInteractor.getWords()
                updateState {
                    SelectTranslateState.Success(
                        words = words,
                        screenState = SelectTranslateScreenState.None,
                    )
                }
                updateWord()
            }
        }
    }

    private fun updateWord() {
        updateState {
            it.mapSuccessState { state ->
                val word = state.words[state.counter]
                val translates = state.words.toMutableList()
                    .shuffled()
                    .map { it.translate }
                state.copy(
                    screenState = SelectTranslateScreenState.SelectWord(word, translates)
                )
            }
        }
    }

    fun onTranslateClick(translate: String) {
        val state = state.value as SelectTranslateState.Success
        val word = (state.screenState as SelectTranslateScreenState.SelectWord).word
        val isSuccess = word.translate == translate
        if (isSuccess) {
            updateState {
                it.mapSuccessState { state ->
                    state.copy(
                        words = state.words.map { originalWord ->
                            if (originalWord.id == word.id) {
                                originalWord.copy(
                                    frequency = originalWord.frequency + WORD_GAME_PRICE,
                                )
                            } else {
                                originalWord
                            }
                        },
                        screenState = SelectTranslateScreenState.Success,
                        successCounter = state.successCounter + 1,
                    )
                }
            }
        } else {
            updateState {
                it.mapSuccessState { state ->
                    state.copy(
                        words = state.words.map { originalWord ->
                            if (originalWord.id == word.id) {
                                originalWord.copy(
                                    frequency = originalWord.frequency - WORD_GAME_PRICE,
                                )
                            } else {
                                originalWord
                            }
                        },
                        screenState = SelectTranslateScreenState.Error,
                        errorCounter = state.errorCounter + 1,
                    )
                }
            }
        }
    }

    fun onAnimationEnd() {
        val stateValue = state.value as SelectTranslateState.Success
        if (stateValue.counter == MAX_WORDS_COUNT_SELECT_TRANSLATE - 1) {
            endGame()
        } else {
            updateState {
                it.mapSuccessState { state ->
                    state.copy(
                        counter = state.counter + 1,
                    )
                }
            }
            updateWord()
        }
    }

    private fun endGame() {
        viewModelScope.launch(
            exceptionHandler { showFinalScreen() },
        ) {
            val stateValue = state.value as SelectTranslateState.Success
            withContext(dispatcherProvider.io + NonCancellable) {
                supervisorScope {
                    val tasks = stateValue.words.map { word ->
                        async { selectTranslateInteractor.saveWord(word) }
                    }
                    tasks.forEach { it.await() }
                }
                showFinalScreen()
            }
        }
    }

    private fun showFinalScreen() {
        updateState {
            it.mapSuccessState { state ->
                state.copy(
                    screenState = SelectTranslateScreenState.Result(
                        successCount = state.successCounter,
                        errorCount = state.errorCounter,
                    )
                )
            }
        }
    }

    fun onBackPressed() {
        rootNavigator.back()
    }
}
