package com.nikcapko.memo.presentation.screens.words.list

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.memo.domain.usecases.WordListUseCase
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.common.emptyExceptionHandler
import com.nikcapko.memo.core.common.exceptionHandler
import com.nikcapko.memo.core.ui.viewmodel.BaseViewModel
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.screens.words.list.event.WordListEvent
import com.nikcapko.memo.presentation.screens.words.list.state.WordListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val MIN_WORDS_COUNT = 5

@HiltViewModel
internal class WordListViewModel @Inject constructor(
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
    private val wordListUseCase: WordListUseCase,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<WordListState, WordListEvent>() {

    override fun createInitialState(): WordListState = WordListState.None

    override fun onViewFirstCreated() {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(
            exceptionHandler { exception ->
                updateState { WordListState.Error("Произошла ошибка") }
            }
        ) {
            withContext(dispatcherProvider.io) {
                updateState { WordListState.Loading }
                delay(5000L) // TODO for test loading animation
                val wordsList = wordListUseCase()
                updateState { WordListState.Success(wordsList) }
            }
        }
    }

    fun onItemClick(position: Int) {
        val wordList = (state.value as? WordListState.Success)?.words
        rootNavigator.pushWordDetailScreen(wordList?.getOrNull(position))
    }

    fun onEnableSound(position: Int) {
        val wordList = (state.value as? WordListState.Success)?.words
        sendEvent(WordListEvent.SpeakOutEvent(wordList?.getOrNull(position)?.word.orEmpty()))
    }

    fun clearDatabase() {
        viewModelScope.launch(emptyExceptionHandler) {
            withContext(dispatcherProvider.io) {
                updateState { WordListState.Loading }
                clearDatabaseUseCase()
                updateState { WordListState.Success(emptyList()) }
            }
        }
    }

    fun onAddWordClick() {
        rootNavigator.pushWordDetailScreen()
    }

    fun onRetryClick() {
        loadWords()
    }

    fun openGamesScreen() {
        val wordList = (state.value as? WordListState.Success)?.words.orEmpty()
        if (wordList.size < MIN_WORDS_COUNT) {
            sendEvent(WordListEvent.ShowNeedMoreWordsEvent)
        } else {
            rootNavigator.pushGamesScreen()
        }
    }

    fun onClearDatabaseClick() {
        sendEvent(WordListEvent.ShowClearDatabaseEvent)
    }
}
