@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.select_translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.FragmentSelectTranslateBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SelectTranslateFragment : MvpAppCompatFragment(), SelectTranslateView {

    companion object {
        const val WORDS = "SelectTranslateFragment.WORDS"
    }

    @Inject
    lateinit var presenterProvider: Provider<SelectTranslatePresenter>
    private val presenter: SelectTranslatePresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentSelectTranslateBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
    }
}
