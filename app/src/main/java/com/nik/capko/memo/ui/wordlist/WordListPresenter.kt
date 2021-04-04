package com.nik.capko.memo.ui.wordlist

import com.nik.capko.memo.db.Word
import com.nik.capko.memo.repository.WordRepository
import moxy.MvpPresenter
import javax.inject.Inject

class WordListPresenter @Inject constructor(
    private val wordRepository: WordRepository
) : MvpPresenter<WordListView>() {

    private var wordsList = emptyList<Word>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        // loadWords()
    }

    private fun loadWords() {
        wordsList = wordRepository.getAllWords()
        viewState.showWords(wordsList)
    }
}
