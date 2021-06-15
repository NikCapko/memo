@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.select_translate

import com.github.terrakok.cicerone.Router
import com.nik.capko.memo.data.Game
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.data.FormDBEntity
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import javax.inject.Inject

class SelectTranslatePresenter @Inject constructor(
    private val router: Router,
    private val gameRepository: GameRepository,
) : MvpPresenter<SelectTranslateView>() {

    private var words: List<Word>? = null
    private var word: Word? = null

    private var counter = 0
    private var errorCounter = 0
    private var successCounter = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initView()
    }

    private fun initView() {
        loadWords()
    }

    fun loadWords() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startLoading()
            }
            words = gameRepository.getWordsForGameByLimit(Game.MAX_WORDS_COUNT_SELECT_TRANSLATE)
            launch(Dispatchers.Main) {
                updateWord()
                viewState.completeLoading()
            }
        }
    }

    private fun updateWord() {
        word = words?.getOrNull(counter)
        val translates = words?.toMutableList()
            ?.shuffled()
            ?.map { it.translation.toString() }
            .orEmpty()
        viewState.showWord(word, translates)
    }

    fun onTranslateClick(translate: String) {
        if (word?.translation.equals(translate)) {
            word?.frequency = word?.frequency?.plus(Word.WORD_GAME_PRICE)
            viewState.showSuccessAnimation()
            successCounter++
        } else {
            word?.frequency = word?.frequency?.minus(Word.WORD_GAME_PRICE)
            viewState.showErrorAnimation()
            errorCounter++
        }
    }

    fun onAnimationEnd() {
        if (counter == Game.MAX_WORDS_COUNT_SELECT_TRANSLATE - 1) {
            updateWordsDB()
        } else {
            counter++
            updateWord()
        }
    }

    private fun updateWordsDB() {
        CoroutineScope(Dispatchers.Default).launch {
            words?.forEach { word ->
                val wordDBEntity = WordDBEntity(
                    word.id,
                    word.word,
                    word.type,
                    word.gender,
                    word.translation,
                    word.frequency,
                    word.primaryLanguage
                )
                gameRepository.saveWord(wordDBEntity)
                word.forms?.forEach { form ->
                    gameRepository.saveForm(
                        FormDBEntity(form.key, form.name, form.value, word.id)
                    )
                }
            }
            launch(Dispatchers.Main) {
                viewState.showEndGame(successCounter, errorCounter)
            }
        }
    }

    fun onBackPressed() {
        router.exit()
    }
}
