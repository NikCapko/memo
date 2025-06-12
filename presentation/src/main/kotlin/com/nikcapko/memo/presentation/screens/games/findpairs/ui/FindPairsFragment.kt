@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.presentation.screens.games.findpairs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikcapko.memo.core.ui.BaseFragment
import com.nikcapko.memo.core.ui.viewmodel.lazyViewModels
import com.nikcapko.memo.presentation.R
import com.nikcapko.memo.presentation.screens.games.findpairs.FindPairsViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("TooManyFunctions")
@AndroidEntryPoint
internal class FindPairsFragment : BaseFragment() {

    private val viewModel by lazyViewModels<FindPairsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_find_pairs, container, false)
    }
}
