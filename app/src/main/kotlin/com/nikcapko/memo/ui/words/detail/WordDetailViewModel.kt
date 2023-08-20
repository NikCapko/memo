package com.nikcapko.memo.ui.words.detail

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.nikcapko.domain.usecases.DeleteWordUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
internal class WordDetailViewModel @Inject constructor(
    private val router: Router,
    private val saveWordUseCase: SaveWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val wordModelMapper: WordModelMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(createInitialState())

    private val state: StateFlow<WordDetailViewState>
        get() = _state.asStateFlow()

    private val _wordState = MutableStateFlow("")
    private val _translateState = MutableStateFlow("")

    val wordState: Flow<Word> = state.mapNotNull { it.word }

    val progressLoadingState: Flow<Boolean> = state.mapNotNull { it.showProgressDialog }

    val enableSaveButtonState: Flow<Boolean>
        get() = combine(_wordState, _translateState) { word, translate ->
            return@combine word.isNotEmpty() && translate.isNotEmpty()
        }

    val successResultChannel = Channel<Unit>()

    fun setArguments(vararg params: Any?) {
        _state.update {
            it.copy(
                word = params[0] as? Word,
            )
        }
    }

    fun onSaveWord(wordArg: String, translate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            launch(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        showProgressDialog = true,
                    )
                }
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
            saveWordUseCase(wordModelMapper.mapToEntity(word))
            launch(Dispatchers.Main) {
                successResultChannel.send(Unit)
                _state.update {
                    it.copy(
                        showProgressDialog = false,
                    )
                }
                router.exit()
            }
        }
    }

    fun onDeleteWord() {
        CoroutineScope(Dispatchers.IO).launch {
            launch(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        showProgressDialog = true,
                    )
                }
            }
            state.value.word?.let { deleteWordUseCase(it.id.toString()) }
            launch(Dispatchers.Main) {
                successResultChannel.send(Unit)
                _state.update {
                    it.copy(
                        showProgressDialog = false,
                    )
                }
                router.exit()
            }
        }
    }

    private fun createInitialState(): WordDetailViewState {
        return WordDetailViewState(
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
