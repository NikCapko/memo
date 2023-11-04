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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {

    private val _state = MutableStateFlow(createInitialState())
    private val state = _state.asStateFlow()

    private val _wordState = MutableStateFlow("")
    private val _translateState = MutableStateFlow("")

    val wordState: Flow<Word> = state.mapNotNull { it.word }
    val progressLoadingState: Flow<Boolean> = state.mapNotNull { it.showProgressDialog }
    val enableSaveButtonState: Flow<Boolean> =
        combine(_wordState, _translateState) { word, translate ->
            return@combine word.isNotEmpty() && translate.isNotEmpty()
        }.distinctUntilChanged()

    private val _eventFlow = MutableSharedFlow<WordDetailsEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun setArguments(vararg params: Any?) {
        _state.update {
            it.copy(word = params[0] as? Word)
        }
    }

    fun onSaveWord(wordArg: String, translate: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) {
                _state.update { it.copy(showProgressDialog = true) }
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
            _eventFlow.emit(WordDetailsEvent.CloseScreenEvent)
            _state.update { it.copy(showProgressDialog = false) }
            withContext(dispatcherProvider.main) {
                navigator.back()
            }
        }
    }

    fun onDeleteWord() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { it.copy(showProgressDialog = true) }
            state.value.word?.let {
                wordDetailsInteractor.deleteWord(it.id.toString())
            }
            _eventFlow.emit(WordDetailsEvent.CloseScreenEvent)
            _state.update { it.copy(showProgressDialog = false) }
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

    fun changeWordField(word: String) {
        _wordState.value = word
    }

    fun changeTranslateField(translate: String) {
        _translateState.value = translate
    }
}
