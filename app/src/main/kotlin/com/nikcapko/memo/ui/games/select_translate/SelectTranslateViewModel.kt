@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.select_translate

import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.base.ui.flow.EventFlowWrapper
import com.nikcapko.memo.base.ui.viewmodel.BaseEventViewModel
import com.nikcapko.memo.data.WORD_GAME_PRICE
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.MAX_WORDS_COUNT_SELECT_TRANSLATE
import com.nikcapko.memo.domain.SelectTranslateInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectTranslateViewModel @Inject constructor(
    private val selectTranslateInteractor: SelectTranslateInteractor,
    private val stateFlowWrapper: SelectTranslateStateFlowWrapper,
    eventFlowWrapper: EventFlowWrapper<SelectTranslateEvent>,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseEventViewModel<SelectTranslateEvent>(eventFlowWrapper, dispatcherProvider),
    SelectTranslateViewController {

    val stateFlow = stateFlowWrapper.liveValue()

    private var words: List<Word>? = null
    private var word: Word? = null

    private var counter = 0
    private var errorCounter = 0
    private var successCounter = 0

    init {
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
        navigator.back()
    }
}
