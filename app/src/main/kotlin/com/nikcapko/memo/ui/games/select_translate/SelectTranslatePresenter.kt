@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.select_translate

import com.github.terrakok.cicerone.Router
import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import com.nikcapko.memo.data.Game
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import javax.inject.Inject

class SelectTranslatePresenter @Inject constructor(
    private val router: Router,
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val saveWordUseCase: SaveWordUseCase,
    private val wordModelMapper: WordModelMapper,
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
        CoroutineScope(Dispatchers.IO).launch {
            launch(Dispatchers.Main) {
                viewState.startLoading()
            }
            words =
                wordModelMapper.mapFromEntityList(gameWordsLimitUseCase(Game.MAX_WORDS_COUNT_SELECT_TRANSLATE))
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
            ?.map { it.translation }
            .orEmpty()
        viewState.showWord(word, translates)
    }

    fun onTranslateClick(translate: String) {
        if (word?.translation.equals(translate)) {
            word?.frequency = word?.frequency?.plus(Word.WORD_GAME_PRICE) ?: Word.WORD_GAME_PRICE
            viewState.showSuccessAnimation()
            successCounter++
        } else {
            word?.frequency = word?.frequency?.minus(Word.WORD_GAME_PRICE) ?: -Word.WORD_GAME_PRICE
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
        CoroutineScope(Dispatchers.IO).launch {
            words?.forEach { word ->
                saveWordUseCase(wordModelMapper.mapToEntity(word))
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
