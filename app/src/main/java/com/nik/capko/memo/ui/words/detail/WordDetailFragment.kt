package com.nik.capko.memo.ui.words.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.FragmentWordDetailBinding
import com.nik.capko.memo.utils.extensions.argument
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class WordDetailFragment : MvpAppCompatFragment(), WordDetailMvpView {

    companion object {
        const val WORD = "WordDetailFragment.WORD"
    }

    @Inject
    lateinit var presenterProvider: Provider<WordDetailPresenter>
    private val presenter: WordDetailPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentWordDetailBinding::bind)

    private var word: Word? by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getArgs()
    }

    private fun getArgs() {
        presenter.setArguments(word)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
    }
}
