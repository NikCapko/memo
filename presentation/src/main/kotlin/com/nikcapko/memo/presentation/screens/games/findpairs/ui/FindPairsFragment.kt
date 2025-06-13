@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.presentation.screens.games.findpairs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import com.nikcapko.memo.core.ui.BaseFragment
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.core.ui.viewmodel.lazyViewModels
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
    ): ComposeView = ComposeView(requireContext()).apply {
        setContent {
            ComposeTheme {
                FindPairsScreen(
                    state = viewModel.state.collectAsState(),
                    onRetry = { viewModel.loadWords() },
                    onWordClick = { viewModel.onWordClick(it) },
                    onTranslateClick = { viewModel.onTranslateClick(it) },
                    endGameClick = { viewModel.onBackPressed() },
                )
            }
        }
    }
}
