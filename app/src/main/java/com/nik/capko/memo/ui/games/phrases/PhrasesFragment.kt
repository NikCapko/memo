package com.nik.capko.memo.ui.games.phrases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.FragmentPhrasesBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class PhrasesFragment : MvpAppCompatFragment(), PhrasesView {

    companion object {
        const val WORDS = "SelectTranslateFragment.WORDS"
    }

    @Inject
    lateinit var presenterProvider: Provider<PhrasesPresenter>
    private val presenter: PhrasesPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentPhrasesBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phrases, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
    }
}
