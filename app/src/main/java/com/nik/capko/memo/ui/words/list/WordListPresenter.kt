package com.nik.capko.memo.ui.words.list

import com.github.terrakok.cicerone.Router
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.repository.WordRepository
import com.nik.capko.memo.ui.Screens
import com.nik.capko.memo.utils.AppStorage
import com.nik.capko.memo.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class WordListPresenter @Inject constructor(
    private val router: Router,
    private val wordRepository: WordRepository,
    private val appStorage: AppStorage,
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
            wordsList = wordRepository.getWordsFromDB()
                .filter { it.primaryLanguage }
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
                wordRepository.clearDatabase()
            }
            launch(Dispatchers.Main) {
                router.replaceScreen(Screens.signInScreen())
            }
        }
    }
}
