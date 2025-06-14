package com.nikcapko.memo.presentation.screens.words.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.core.common.Constants
import com.nikcapko.memo.core.ui.BaseFragment
import com.nikcapko.memo.core.ui.extensions.argument
import com.nikcapko.memo.core.ui.extensions.observe
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.core.ui.viewmodel.lazyViewModels
import com.nikcapko.memo.presentation.screens.words.details.WordDetailsViewModel
import com.nikcapko.memo.presentation.screens.words.details.event.WordDetailsEvent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

@AndroidEntryPoint
internal class WordDetailsFragment : BaseFragment() {

    companion object {
        const val WORD_ARGUMENT = "WordDetailFragment.WORD"
    }

    private val word by argument<WordModel>(WORD_ARGUMENT)

    private val viewModel by lazyViewModels<WordDetailsViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras
                .withCreationCallback<WordDetailsViewModel.Factory> { factory ->
                    factory.create(word)
                }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ComposeView = ComposeView(requireContext()).apply {
        setContent {
            ComposeTheme {
                WordDetailScreen(
                    state = viewModel.state.collectAsState(),
                    changeWordField = { viewModel.changeWordField(it) },
                    changeTranslateField = { viewModel.changeTranslateField(it) },
                    onAddWord = { viewModel.onSaveWord() },
                    onChangeWord = { viewModel.onSaveWord() },
                    onDeleteWord = { viewModel.onDeleteWord() },
                    onBackPressed = { viewModel.onBackPressed() },
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        observe(viewModel.eventFlow, ::observeEvents)
    }

    private fun observeEvents(event: WordDetailsEvent) = when (event) {
        is WordDetailsEvent.CloseScreenEvent -> sendSuccessResult()
    }

    private fun sendSuccessResult() {
        parentFragmentManager.setFragmentResult(Constants.LOAD_WORDS_EVENT, bundleOf())
    }
}
