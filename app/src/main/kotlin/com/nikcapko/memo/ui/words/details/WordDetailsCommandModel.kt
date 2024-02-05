package com.nikcapko.memo.ui.words.details

import androidx.lifecycle.viewModelScope
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.base.ui.flow.EventFlowWrapper
import com.nikcapko.memo.base.ui.viewmodel.BaseEventViewModel
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.WordDetailsInteractor
import com.nikcapko.memo.navigation.Navigator
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
    private val navigator: Navigator,
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
                navigator.back()
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
