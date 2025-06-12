package com.nikcapko.memo.presentation.games.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import com.nikcapko.memo.core.ui.BaseFragment
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.core.ui.viewmodel.lazyViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesFragment : BaseFragment() {

    private val viewModel by lazyViewModels<GamesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ComposeView = ComposeView(requireContext()).apply {
        setContent {
            ComposeTheme {
                GamesScreen(
                    state = viewModel.state.collectAsState(),
                    onItemClicked = { viewModel.onItemClick(it) },
                    onBackPressed = { viewModel.onBackPressed() },
                )
            }
        }
    }
}
