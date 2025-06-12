@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.presentation.games.selecttranslate

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.core.ui.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.ui.viewmodel.BaseEventViewModel
import com.nikcapko.memo.presentation.domain.MAX_WORDS_COUNT_SELECT_TRANSLATE
import com.nikcapko.memo.presentation.domain.SelectTranslateInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal const val WORD_GAME_PRICE = 0.02f

@HiltViewModel
internal class SelectTranslateViewModel @Inject constructor(
    private val selectTranslateInteractor: SelectTranslateInteractor,
    private val stateFlowWrapper: SelectTranslateStateFlowWrapper,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseEventViewModel<SelectTranslateEvent>(), SelectTranslateCommandReceiver {

    val stateFlow = stateFlowWrapper.liveValue()

    private var words: List<Word>? = null
    private var word: Word? = null

    private var counter = 0
    private var errorCounter = 0
    private var successCounter = 0

    override fun onViewFirstCreated() {
        loadWords()
    }

    override fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            stateFlowWrapper.update(DataLoadingViewModelState.LoadingState)
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
        stateFlowWrapper.update(DataLoadingViewModelState.LoadedState(word to translates))
    }

    override fun onTranslateClick(translate: String) {
        if (word?.translation.equals(translate)) {
            word?.frequency = word?.frequency?.plus(WORD_GAME_PRICE) ?: WORD_GAME_PRICE
            sendEvent(SelectTranslateEvent.SuccessAnimationEvent)
            successCounter++
        } else {
            word?.frequency = word?.frequency?.minus(WORD_GAME_PRICE) ?: -WORD_GAME_PRICE
            sendEvent(SelectTranslateEvent.ErrorAnimationEvent)
            errorCounter++
        }
    }

    override fun onAnimationEnd() {
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
            sendEvent(SelectTranslateEvent.EndGameEvent(successCounter, errorCounter))
        }
    }

    override fun onBackPressed() {
        rootNavigator.back()
    }
}
