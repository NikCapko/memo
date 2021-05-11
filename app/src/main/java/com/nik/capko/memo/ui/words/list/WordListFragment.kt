package com.nik.capko.memo.ui.words.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.FragmentWordListBinding
import com.nik.capko.memo.ui.base.MainActivity
import com.nik.capko.memo.ui.words.detail.WordDetailFragment
import com.nik.capko.memo.ui.words.list.adapter.WordListAdapter
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class WordListFragment @Inject constructor() : MvpAppCompatFragment(), WordListMvpView {

    @Inject
    lateinit var presenterProvider: Provider<WordListPresenter>
    private val presenter: WordListPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentWordListBinding::bind)

    private lateinit var adapter: WordListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            adapter = this@WordListFragment.adapter
        }
    }

    override fun showWords(wordsList: List<Word>) {
        adapter.words = wordsList
        Toast.makeText(context, "${wordsList.size}", Toast.LENGTH_SHORT).show()
    }

    override fun showWordDetail(word: Word?) {
        val bundle = bundleOf(
            WordDetailFragment.WORD to word,
        )
        (activity as? MainActivity)?.openFragment(WordDetailFragment::class.java, bundle)
    }

    override fun openAddWordScreen() {
        (activity as? MainActivity)?.openFragment(WordDetailFragment::class.java)
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
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun completeLoading() {
        with(viewBinding) {
            pvLoad.completeLoading()
            rvWords.makeVisible()
            btnAddWord.makeVisible()
        }
    }
}
