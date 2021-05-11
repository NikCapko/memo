package com.nik.capko.memo.ui.words.detail

import com.nik.capko.memo.data.Word
import moxy.MvpPresenter
import javax.inject.Inject

class WordDetailPresenter @Inject constructor() : MvpPresenter<WordDetailMvpView>() {

    private var word: Word? = null

    fun setArguments(vararg params: Any?) {
        word = params[0] as? Word
    }
}
