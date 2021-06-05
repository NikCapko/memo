@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.find_pairs

import com.nik.capko.memo.data.Game
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import javax.inject.Inject

class FindPairsPresenter @Inject constructor(
    private val wordRepository: WordRepository
) : MvpPresenter<FindPairsView>() {

    private var words = emptyList<Word>()

    private var wordsCount = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initView()
    }

    private fun initView() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startLoading()
            }
            words = wordRepository.getWordsForGameFromDB()
                .filter { it.primaryLanguage }

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
}
