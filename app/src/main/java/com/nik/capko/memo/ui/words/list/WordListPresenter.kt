package com.nik.capko.memo.ui.words.list

import com.nik.capko.memo.app.appStorage
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.repository.WordRepository
import com.nik.capko.memo.utils.Constants
import com.nik.capko.memo.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class WordListPresenter @Inject constructor(
    private val wordRepository: WordRepository
) : MvpPresenter<WordListMvpView>() {

    private var wordsList = emptyList<Word>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadWords()
    }

    private fun loadWords() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startLoading()
            }
            delay(2000)
            // val resource =
            /* val word = WordDBEntity(
                 Date().time,
                 "a",
                 "t",
                 "f",
                 0f
             )
             wordRepository.saveWord(word)
             wordRepository.saveForm(FormDBEntity("key", "h", "k", word.id))*/
            wordsList = wordRepository.getWordsFromDB()
            launch(Dispatchers.Main) {
                viewState.showWords(wordsList)
                viewState.completeLoading()
            }
            // checkWordsResponse(resource)
        }
    }

    private fun CoroutineScope.checkWordsResponse(resource: Resource<List<Word>>) = launch {
        when (resource.status) {
            Resource.Status.SUCCESS -> {
                wordsList = resource.data as List<Word>
                launch(Dispatchers.Main) {
                    viewState.showWords(wordsList)
                }
            }
            Resource.Status.ERROR -> {
                launch(Dispatchers.Main) {
                    viewState.errorLoading(resource.message)
                }
            }
        }
    }

    fun onItemClick(position: Int) {
        val word = wordsList.getOrNull(position)
        viewState.showWordDetail(word)
    }

    fun onAddWordClick() {
        viewState.showAddWordScreen()
    }

    fun openGamesScreen() {
        viewState.showGamesScreen()
    }

    fun openDictionaryScreen() {
        viewState.showDictionaryScreen()
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
                viewState.openSignInScreen()
            }
        }
    }
}
