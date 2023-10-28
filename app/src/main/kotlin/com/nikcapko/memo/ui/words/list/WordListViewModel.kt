package com.nikcapko.memo.ui.words.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.WordListInteractor
import com.nikcapko.memo.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.ar2code.mutableliveevent.MutableLiveEvent
import javax.inject.Inject

private const val MIN_WORDS_COUNT = 5

@HiltViewModel
internal class WordListViewModel @Inject constructor(
    private val wordListInteractor: WordListInteractor,
    private val navigator: Navigator,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _state =
        MutableStateFlow<DataLoadingViewModelState>(DataLoadingViewModelState.LoadingState)
    val state: Flow<DataLoadingViewModelState> = _state.asStateFlow()

    private val _speakOutEvent = MutableLiveEvent<WordListEvent.SpeakOutEvent>()
    val speakOutEvent: LiveData<WordListEvent.SpeakOutEvent> = _speakOutEvent

    private val _showClearDatabaseDialog = MutableLiveEvent<WordListEvent.ShowClearDatabaseEvent>()
    val showClearDatabaseDialog: LiveData<WordListEvent.ShowClearDatabaseEvent> =
        _showClearDatabaseDialog

    private val _showNeedMoreWordsDialog = MutableLiveEvent<WordListEvent.ShowNeedMoreWordsEvent>()
    val showNeedMoreWordsDialog: LiveData<WordListEvent.ShowNeedMoreWordsEvent> =
        _showNeedMoreWordsDialog

    private var wordsList = emptyList<Word>()

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { DataLoadingViewModelState.LoadingState }
            wordsList = wordListInteractor.getWords()
            _state.update { DataLoadingViewModelState.LoadedState(wordsList) }
        }
    }

    fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        navigator.pushWordDetailScreen(word)
    }

    fun onEnableSound(position: Int) {
        val word = wordsList.getOrNull(position)
        _speakOutEvent.postValue(WordListEvent.SpeakOutEvent(word?.word.orEmpty()))
    }

    fun clearDatabase() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { DataLoadingViewModelState.LoadingState }
            wordListInteractor.clearDataBase()
            _state.update { DataLoadingViewModelState.LoadedState(emptyList<Word>()) }
        }
    }

    fun onAddWordClick() {
        navigator.pushWordDetailScreen()
    }

    fun openGamesScreen() {
        if (wordsList.size < MIN_WORDS_COUNT) {
            _showNeedMoreWordsDialog.postValue(WordListEvent.ShowNeedMoreWordsEvent)
        } else {
            navigator.pushGamesScreen()
        }
    }

    fun onClearDatabaseClick() {
        _showClearDatabaseDialog.postValue(WordListEvent.ShowClearDatabaseEvent)
    }
}
