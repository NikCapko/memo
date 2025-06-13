package com.nikcapko.memo.presentation.screens.words.details

import androidx.lifecycle.viewModelScope
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.common.emptyExceptionHandler
import com.nikcapko.memo.core.ui.viewmodel.BaseViewModel
import com.nikcapko.memo.presentation.domain.WordDetailsInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.screens.words.details.event.WordDetailsEvent
import com.nikcapko.memo.presentation.screens.words.details.state.WordDetailsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@HiltViewModel(assistedFactory = WordDetailsViewModel.Factory::class)
internal class WordDetailsViewModel @AssistedInject constructor(
    @Assisted private val word: WordModel?,
    private val wordDetailsInteractor: WordDetailsInteractor,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<WordDetailsState, WordDetailsEvent>() {

    @AssistedFactory
    fun interface Factory {
        fun create(word: WordModel?): WordDetailsViewModel
    }

    override fun createInitialState(): WordDetailsState {
        return WordDetailsState(
            word = word ?: WordModel(
                id = Date().time,
                word = "",
                translate = "",
                frequency = 0f,
            ),
            isAddNewWord = word == null,
            enableSaveButton = word != null,
            showProgressDialog = false,
        )
    }

    fun onSaveWord() {
        viewModelScope.launch(emptyExceptionHandler) {
            withContext(dispatcherProvider.io) {
                updateState { it.copy(showProgressDialog = true) }
                wordDetailsInteractor.saveWord(state.value.word)
                sendEvent(WordDetailsEvent.CloseScreenEvent)
                updateState { it.copy(showProgressDialog = false) }
            }
            withContext(dispatcherProvider.main) {
                rootNavigator.back()
            }
        }
    }

    fun onDeleteWord() {
        viewModelScope.launch(emptyExceptionHandler) {
            withContext(dispatcherProvider.io) {
                updateState { it.copy(showProgressDialog = true) }
                wordDetailsInteractor.deleteWord(state.value.word.id.toString())
                sendEvent(WordDetailsEvent.CloseScreenEvent)
                updateState { it.copy(showProgressDialog = false) }
            }
            withContext(dispatcherProvider.main) {
                rootNavigator.back()
            }
        }
    }

    fun changeWordField(word: String) {
        updateState {
            val word = it.word.copy(word = word)
            it.copy(
                word = word,
                enableSaveButton = word.word.isNotBlank() && word.translate.isNotBlank(),
            )
        }
    }

    fun changeTranslateField(translate: String) {
        updateState {
            val word = it.word.copy(translate = translate)
            it.copy(
                word = word,
                enableSaveButton = word.word.isNotBlank() && word.translate.isNotBlank(),
            )
        }
    }

    fun onBackPressed() {
        rootNavigator.back()
    }
}
