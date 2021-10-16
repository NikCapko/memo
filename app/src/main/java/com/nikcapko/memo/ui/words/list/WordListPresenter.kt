package com.nikcapko.memo.ui.words.list

import com.github.terrakok.cicerone.Router
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.ui.Screens
import com.nikcapko.memo.usecases.ClearDatabaseUseCase
import com.nikcapko.memo.usecases.PrimaryWordListUseCase
import com.nikcapko.memo.utils.AppStorage
import com.nikcapko.memo.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class WordListPresenter @Inject constructor(
    private val router: Router,
    private val appStorage: AppStorage,
    private val primaryWordListUseCase: PrimaryWordListUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
) : MvpPresenter<WordListView>() {

    private var wordsList = emptyList<Word>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadWords()
    }

    fun loadWords() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startLoading()
            }
            wordsList = primaryWordListUseCase.getWordList()
            launch(Dispatchers.Main) {
                viewState.showWords(wordsList)
                viewState.completeLoading()
            }
        }
    }

    fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        router.navigateTo(Screens.wordDetailScreen(word))
    }

    fun onEnableSound(position: Int) {
        val word = wordsList.getOrNull(position)
        viewState.speakOut(word?.word)
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
        viewState.showClearDatabaseDialog()
    }

    fun logout(clearDataBase: Boolean) {
        appStorage.put(Constants.IS_REGISTER, false)
        CoroutineScope(Dispatchers.Default).launch {
            if (clearDataBase) {
                clearDatabaseUseCase.clearDatabase()
            }
            launch(Dispatchers.Main) {
                router.replaceScreen(Screens.signInScreen())
            }
        }
    }
}
