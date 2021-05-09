package com.nik.capko.memo.ui.words.detail

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.ActivityWordDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class WordDetailActivity : MvpAppCompatActivity(), WordDetailMvpView {

    companion object {
        const val WORD = "WordDetailActivity.WORD"
    }

    @Inject
    lateinit var presenterProvider: Provider<WordDetailPresenter>
    private val presenter: WordDetailPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(ActivityWordDetailBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_detail)
        setListeners()
    }

    private fun setListeners() {
    }
}
