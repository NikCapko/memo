package com.nikcapko.memo.presentation.words.details

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.core.common.DispatcherProvider
import com.nikcapko.memo.core.common.data.Word
import com.nikcapko.memo.core.navigation.RootNavigator
import com.nikcapko.memo.core.ui.flow.EventFlowWrapper
import com.nikcapko.memo.core.ui.viewmodel.BaseEventViewModel
import com.nikcapko.memo.presentation.domain.WordDetailsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
internal class WordDetailsCommandModel @Inject constructor(
    private val wordDetailsInteractor: WordDetailsInteractor,
    private val stateFlowWrapper: WordDetailsStateFlowWrapper,
    eventFlowWrapper: EventFlowWrapper<WordDetailsEvent>,
    private val rootNavigator: RootNavigator,
    private val dispatcherProvider: DispatcherProvider,
) : BaseEventViewModel<WordDetailsEvent>(eventFlowWrapper, dispatcherProvider),
    WordDetailsCommandReceiver {

    private val fieldWordState = MutableStateFlow("")
    private val fieldTranslateState = MutableStateFlow("")

    val wordState = stateFlowWrapper.liveValue().mapNotNull { it.word }
    val progressLoadingState = stateFlowWrapper.liveValue().mapNotNull { it.showProgressDialog }
    val enableSaveButtonState = combine(fieldWordState, fieldTranslateState) { word, translate ->
        return@combine word.isNotEmpty() && translate.isNotEmpty()
    }.distinctUntilChanged()

    init {
        stateFlowWrapper.update(createInitialState())
    }

    override fun setArguments(vararg params: Any?) {
        stateFlowWrapper.update {
            it.copy(word = params[0] as? Word)
        }
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
