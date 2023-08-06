package com.nikcapko.memo.ui.words.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import com.nikcapko.memo.ui.Screens
import com.nikcapko.memo.utils.AppStorage
import com.nikcapko.memo.utils.Constants
import com.nikcapko.memo.utils.extensions.default
import com.nikcapko.memo.utils.extensions.set
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ar2code.mutableliveevent.Event
import ru.ar2code.mutableliveevent.EventArgs
import ru.ar2code.mutableliveevent.MutableLiveEvent
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val router: Router,
    private val appStorage: AppStorage,
    private val wordListUseCase: WordListUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
    private val wordModelMapper: WordModelMapper,
) : ViewModel() {

    private val _state =
        MutableLiveData<DataLoadingViewModelState>().default(initialValue = DataLoadingViewModelState.LoadingState)
    val dataLoadingViewModelState: LiveData<DataLoadingViewModelState>
        get() = _state

    private val _speakOut = MutableLiveEvent<SpeakOutEvent>()
    val speakOut: LiveData<SpeakOutEvent>
        get() = _speakOut

    private val _showClearDatabaseDialog = MutableLiveEvent<ShowClearDatabaseDialogEvent>()
    val showClearDatabaseDialog: LiveData<ShowClearDatabaseDialogEvent>
        get() = _showClearDatabaseDialog

    private var wordsList = emptyList<Word>()

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch {
            _state.postValue(DataLoadingViewModelState.LoadingState)
            wordsList = wordModelMapper.mapFromEntityList(wordListUseCase())
            _state.postValue(DataLoadingViewModelState.LoadedState(wordsList))
        }
    }

    fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        router.navigateTo(Screens.wordDetailScreen(word))
    }

    fun onEnableSound(position: Int) {
        val word = wordsList.getOrNull(position)
        _speakOut.set(SpeakOutEvent(word?.word.orEmpty()))
    }

    fun onAddWordClick() {
        router.navigateTo(Screens.wordDetailScreen(null))
    }

    fun openGamesScreen() {
        router.navigateTo(Screens.gamesScreen())
    }

    fun logout(clearDataBase: Boolean) {
        viewModelScope.launch {
            if (clearDataBase) {
                clearDatabaseUseCase()
            }
        }
    }

    class SpeakOutEvent(data: String) : EventArgs<String>(data)

    object ShowClearDatabaseDialogEvent : Event()
}
