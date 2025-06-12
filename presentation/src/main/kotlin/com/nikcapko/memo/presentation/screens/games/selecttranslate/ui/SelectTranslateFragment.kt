package com.nikcapko.memo.presentation.screens.games.selecttranslate.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import com.nikcapko.memo.core.ui.BaseFragment
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.core.ui.viewmodel.lazyViewModels
import com.nikcapko.memo.presentation.screens.games.selecttranslate.SelectTranslateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SelectTranslateFragment : BaseFragment() {

    private val viewModel by lazyViewModels<SelectTranslateViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ComposeView = ComposeView(requireContext()).apply {
        setContent {
            ComposeTheme {
                SelectTranslateScreen(
                    state = viewModel.state.collectAsState(),
                    onRetry = { viewModel.loadWords() },
                    onTranslateClick = { viewModel.onTranslateClick(it) },
                    onBackPressed = { viewModel.onBackPressed() },
                    animationEnd = { viewModel.onAnimationEnd() },
                    endGameClick = { viewModel.onBackPressed() },
                )
            }
        }
    }
}
