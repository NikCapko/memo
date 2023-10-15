package com.nikcapko.memo.ui.words.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.ar2code.mutableliveevent.Event
import ru.ar2code.mutableliveevent.EventArgs
import ru.ar2code.mutableliveevent.MutableLiveEvent
import javax.inject.Inject

@HiltViewModel
internal class WordListViewModel @Inject constructor(
    private val navigator: Navigator,
    private val wordListUseCase: WordListUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
    private val wordModelMapper: WordModelMapper,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: Flow<DataLoadingViewModelState> = _state.asStateFlow()

    private val _speakOutEvent = MutableLiveEvent<EventArgs<String>>()
    val speakOutEvent: LiveData<EventArgs<String>>
        get() = _speakOutEvent

    private val _showClearDatabaseDialog = MutableLiveEvent<Event>()
    val showClearDatabaseDialog: LiveData<Event>
        get() = _showClearDatabaseDialog

    private var wordsList = emptyList<Word>()

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { DataLoadingViewModelState.LoadingState }
            val wordModelList = wordListUseCase()
            wordsList = wordModelMapper.mapFromEntityList(wordModelList)
            _state.update { DataLoadingViewModelState.LoadedState(wordsList) }
        }
    }

    fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        navigator.pushWordDetailScreen(word)
    }

    fun onEnableSound(position: Int) {
        val word = wordsList.getOrNull(position)
        _speakOutEvent.postValue(EventArgs(word?.word.orEmpty()))
    }

    fun clearDatabase() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { DataLoadingViewModelState.LoadingState }
            clearDatabaseUseCase.invoke()
            _state.update { DataLoadingViewModelState.LoadedState(emptyList<Word>()) }
        }
    }

    fun onAddWordClick() {
        navigator.pushWordDetailScreen()
    }

    fun openGamesScreen() {
        navigator.pushGamesScreen()
    }

    fun onClearDatabaseClick() {
        _showClearDatabaseDialog.postValue(Event())
    }
}
