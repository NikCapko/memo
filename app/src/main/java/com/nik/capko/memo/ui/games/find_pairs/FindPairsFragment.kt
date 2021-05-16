package com.nik.capko.memo.ui.games.find_pairs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.FragmentFindPairsBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class FindPairsFragment : MvpAppCompatFragment(), FindPairsView {

    companion object {
        const val WORDS = "SelectTranslateFragment.WORDS"
    }

    @Inject
    lateinit var presenterProvider: Provider<FindPairsPresenter>
    private val presenter: FindPairsPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentFindPairsBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_pairs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
    }
}
