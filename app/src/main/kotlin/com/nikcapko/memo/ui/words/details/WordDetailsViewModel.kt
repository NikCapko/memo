package com.nikcapko.memo.ui.words.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.WordDetailsInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
internal class WordDetailsViewModel @Inject constructor(
    private val wordDetailsInteractor: WordDetailsInteractor,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(), WordDetailsFlowWrapper, WordDetailsViewController {

    private val state = MutableStateFlow(createInitialState())

    private val fieldWordState = MutableStateFlow("")
    private val fieldTranslateState = MutableStateFlow("")

    override val wordState: Flow<Word> = state.mapNotNull { it.word }
    override val progressLoadingState: Flow<Boolean> = state.mapNotNull { it.showProgressDialog }
    override val enableSaveButtonState: Flow<Boolean> =
        combine(fieldWordState, fieldTranslateState) { word, translate ->
            return@combine word.isNotEmpty() && translate.isNotEmpty()
        }
            .distinctUntilChanged()

    override val eventFlow = MutableSharedFlow<WordDetailsEvent>()

    override fun setArguments(vararg params: Any?) {
        state.update {
            it.copy(word = params[0] as? Word)
        }
    }

    override fun onSaveWord(wordArg: String, translate: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) {
                state.update { it.copy(showProgressDialog = true) }
            }
            val word: Word = state.value.word?.let {
                it.apply {
                    word = wordArg
                    translation = translate
                }
            } ?: run {
                Word(
                    id = Date().time,
                    word = wordArg,
                    translation = translate,
                    frequency = 0f,
                )
            }
            wordDetailsInteractor.saveWord(word)
            eventFlow.emit(WordDetailsEvent.CloseScreenEvent)
            state.update { it.copy(showProgressDialog = false) }
            withContext(dispatcherProvider.main) {
                navigator.back()
            }
        }
    }

    override fun onDeleteWord() {
        viewModelScope.launch(dispatcherProvider.io) {
            state.update { it.copy(showProgressDialog = true) }
            state.value.word?.let {
                wordDetailsInteractor.deleteWord(it.id.toString())
            }
            eventFlow.emit(WordDetailsEvent.CloseScreenEvent)
            state.update { it.copy(showProgressDialog = false) }
            withContext(dispatcherProvider.main) {
                navigator.back()
            }
        }
    }

    private fun createInitialState(): WordDetailsViewState {
        return WordDetailsViewState(
            word = null,
            showProgressDialog = false,
            enableSaveButton = false,
        )
    }

    override fun changeWordField(word: String) {
        fieldWordState.value = word
    }

    override fun changeTranslateField(translate: String) {
        fieldTranslateState.value = translate
    }
}
