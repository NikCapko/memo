package com.nikcapko.memo.presentation.words.details

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.ui.viewmodel.BaseEventViewModel
import com.nikcapko.memo.presentation.domain.WordDetailsInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@HiltViewModel(assistedFactory = WordDetailsViewModel.Factory::class)
internal class WordDetailsViewModel @AssistedInject constructor(
    @Assisted private val word: Word?,
    private val wordDetailsInteractor: WordDetailsInteractor,
    private val stateFlowWrapper: WordDetailsStateFlowWrapper,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseEventViewModel<WordDetailsEvent>(dispatcherProvider),
    WordDetailsCommandReceiver {

    @AssistedFactory
    fun interface Factory {
        fun create(word: Word?): WordDetailsViewModel
    }

    private val fieldWordState = MutableStateFlow("")
    private val fieldTranslateState = MutableStateFlow("")

    val wordState = stateFlowWrapper.liveValue().mapNotNull { it.word }
    val progressLoadingState = stateFlowWrapper.liveValue().mapNotNull { it.showProgressDialog }
    val enableSaveButtonState = combine(fieldWordState, fieldTranslateState) { word, translate ->
        return@combine word.isNotEmpty() && translate.isNotEmpty()
    }.distinctUntilChanged()

    override fun onViewFirstCreated() {
        stateFlowWrapper.update(createInitialState(word))
    }

    override fun onSaveWord(wordArg: String, translate: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) {
                stateFlowWrapper.update { it.copy(showProgressDialog = true) }
            }
            val word: Word = stateFlowWrapper.value().word?.let {
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
            sendEvent(WordDetailsEvent.CloseScreenEvent)
            stateFlowWrapper.update { it.copy(showProgressDialog = false) }
            withContext(dispatcherProvider.main) {
                rootNavigator.back()
            }
        }
    }

    override fun onDeleteWord() {
        viewModelScope.launch(dispatcherProvider.io) {
            stateFlowWrapper.update { it.copy(showProgressDialog = true) }
            stateFlowWrapper.value().word?.let {
                wordDetailsInteractor.deleteWord(it.id.toString())
            }
            sendEvent(WordDetailsEvent.CloseScreenEvent)
            stateFlowWrapper.update { it.copy(showProgressDialog = false) }
            withContext(dispatcherProvider.main) {
                rootNavigator.back()
            }
        }
    }

    private fun createInitialState(word: Word?): WordDetailsViewState {
        return WordDetailsViewState(
            word = word,
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
