package com.nik.capko.memo.ui.words.add

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.ActivityWordAddBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class WordAddActivity : MvpAppCompatActivity(), WordAddMvpView {

    @Inject
    lateinit var presenterProvider: Provider<WordAddPresenter>
    private val presenter: WordAddPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(ActivityWordAddBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_add)
        setListeners()
    }

    private fun setListeners() {
    }
}
