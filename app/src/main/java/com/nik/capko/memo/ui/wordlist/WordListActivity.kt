package com.nik.capko.memo.ui.wordlist

import android.os.Bundle
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.ActivityWordListBinding
import com.nik.capko.memo.db.Word
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class WordListActivity : MvpAppCompatActivity(), WordListView {

    @Inject
    lateinit var presenterProvider: Provider<WordListPresenter>
    private val presenter: WordListPresenter by moxyPresenter { presenterProvider.get() }

    // private val viewBinding by viewBinding(ActivityWordListBinding::bind, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
    }

    override fun showWords(wordsList: List<Word>) {
    }
}
