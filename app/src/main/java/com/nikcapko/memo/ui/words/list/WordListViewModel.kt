package com.nikcapko.memo.ui.words.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.ui.Screens
import com.nikcapko.memo.usecases.ClearDatabaseUseCase
import com.nikcapko.memo.usecases.PrimaryWordListUseCase
import com.nikcapko.memo.utils.AppStorage
import com.nikcapko.memo.utils.Constants
import com.nikcapko.memo.utils.extensions.default
import com.nikcapko.memo.utils.extensions.set
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val router: Router,
    private val appStorage: AppStorage,
    private val primaryWordListUseCase: PrimaryWordListUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<State>().default(initialValue = State.LoadingState)
    val state: LiveData<State>
        get() = _state

    private var wordsList = emptyList<Word>()

    init {
        loadWords()
    }

    fun loadWords() {
        viewModelScope.launch {
            _state.postValue(State.LoadingState)
            wordsList = primaryWordListUseCase.getWordList()
            _state.postValue(State.LoadedState(wordsList))
        }
    }

    fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        router.navigateTo(Screens.wordDetailScreen(word))
    }

    fun onEnableSound(position: Int) {
        val word = wordsList.getOrNull(position)
        _state.set(State.SpeakOut(word?.word))
    }

    fun onAddWordClick() {
        router.navigateTo(Screens.wordDetailScreen(null))
    }

    fun openGamesScreen() {
        router.navigateTo(Screens.gamesScreen())
    }

    fun openDictionaryScreen() {
        router.navigateTo(Screens.dictionaryScreen())
    }

    fun logout() {
        _state.set(State.ShowDialogState)
    }

    fun logout(clearDataBase: Boolean) {
        appStorage.put(Constants.IS_REGISTER, false)
        viewModelScope.launch {
            if (clearDataBase) {
                clearDatabaseUseCase.clearDatabase()
            }
            launch(Dispatchers.Main) {
                router.replaceScreen(Screens.signInScreen())
            }
        }
    }

    sealed class State {
        object LoadingState : State()
        object NoItemsState : State()
        data class LoadedState<T>(var data: List<T>?) : State()
        data class ErrorState(var exception: Throwable) : State()
        data class SpeakOut(var word: String?) : State()
        object ShowDialogState : State()
    }
}