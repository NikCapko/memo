package com.nik.capko.memo.ui.words.list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.ActivityWordListBinding
import com.nik.capko.memo.ui.words.add.WordAddActivity
import com.nik.capko.memo.ui.words.detail.WordDetailActivity
import com.nik.capko.memo.ui.words.list.adapter.WordListAdapter
import com.nik.capko.memo.utils.Constants
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class WordListActivity : MvpAppCompatActivity(), WordListMvpView {

    @Inject
    lateinit var presenterProvider: Provider<WordListPresenter>
    private val presenter: WordListPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(ActivityWordListBinding::bind)

    private lateinit var adapter: WordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        setListeners()
        initAdapters()
    }

    private fun setListeners() {
        viewBinding.btnAddWord.setOnClickListener { presenter.onAddWordClick() }
    }

    private fun initAdapters() {
        adapter = WordListAdapter { position ->
            presenter.onItemClick(position)
        }
        viewBinding.rvWords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WordListActivity.adapter
        }
    }

    override fun showWords(wordsList: List<Word>) {
        adapter.words = wordsList
        Toast.makeText(this, "${wordsList.size}", Toast.LENGTH_SHORT).show()
    }

    override fun showWordDetail(word: Word?) {
        val intent = Intent(this, WordDetailActivity::class.java).apply {
            putExtra(WordDetailActivity.WORD, word)
        }
        startActivityForResult(intent, Constants.START_WORD_DETAIL_SCREEN)
    }

    override fun openAddWordScreen() {
        val intent = Intent(this, WordAddActivity::class.java)
        startActivityForResult(intent, Constants.START_WORD_ADD_SCREEN)
    }

    override fun startLoading() {
        with(viewBinding) {
            pvLoad.startLoading()
            rvWords.makeGone()
            btnAddWord.makeGone()
        }
    }

    override fun errorLoading(errorMessage: String?) {
        with(viewBinding) {
            pvLoad.errorLoading(errorMessage)
            rvWords.makeGone()
            btnAddWord.makeGone()
        }
        Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
    }

    override fun completeLoading() {
        with(viewBinding) {
            pvLoad.completeLoading()
            rvWords.makeVisible()
            btnAddWord.makeVisible()
        }
    }
}
