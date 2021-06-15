@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.find_pairs

import com.github.terrakok.cicerone.Router
import com.nik.capko.memo.data.Game
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import javax.inject.Inject

class FindPairsPresenter @Inject constructor(
    private val router: Router,
    private val gameRepository: GameRepository,
) : MvpPresenter<FindPairsView>() {

    private var words = emptyList<Word>()

    private var wordsCount = 0

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
            words = gameRepository.getWordsForGameByLimit(Game.MAX_WORDS_COUNT_FIND_PAIRS)

            val wordList = words.toMutableList()
                .map { it.word }
                .shuffled()

            val translateList = words.toMutableList()
                .map { it.translation }
                .shuffled()

            launch(Dispatchers.Main) {
                viewState.showWords(wordList, translateList)
                viewState.completeLoading()
            }
        }
    }

    fun onFindPair(selectedWord: String, selectedTranslate: String) {
        words.forEach {
            if (it.word.equals(selectedWord) && it.translation.equals(selectedTranslate)) {
                viewState.onFindPairResult(true)
                wordsCount++
                if (wordsCount == Game.MAX_WORDS_COUNT_FIND_PAIRS * 2) {
                    viewState.endGame()
                }
            }
        }
        viewState.onFindPairResult(false)
    }

    fun onBackPressed() {
        router.exit()
    }
}
