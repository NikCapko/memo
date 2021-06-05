package com.nik.capko.memo.ui.words.detail

import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import java.util.Date
import javax.inject.Inject

class WordDetailPresenter @Inject constructor(
    private val wordRepository: WordRepository
) : MvpPresenter<WordDetailView>() {

    private var word: Word? = null

    fun setArguments(vararg params: Any?) {
        word = params[0] as? Word
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initView()
    }

    private fun initView() {
        viewState.initView(word)
    }

    fun onSaveWord(word: String, translate: String) {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startProgressDialog()
            }
            val wordDBEntity = WordDBEntity(
                Date().time,
                word,
                null,
                null,
                translate,
                null,
                true
            )
            wordRepository.saveWord(wordDBEntity)
            launch(Dispatchers.Main) {
                viewState.completeProgressDialog()
                viewState.sendSuccessResult()
                viewState.onCloseScreen()
            }
        }
    }

    fun onDeleteWord() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startProgressDialog()
            }
            wordRepository.deleteWordById(word?.id ?: 0)
            launch(Dispatchers.Main) {
                viewState.completeProgressDialog()
                viewState.sendSuccessResult()
                viewState.onCloseScreen()
            }
        }
    }
}
