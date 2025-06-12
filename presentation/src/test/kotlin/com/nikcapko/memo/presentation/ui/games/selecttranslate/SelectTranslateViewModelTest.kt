package com.nikcapko.memo.presentation.ui.games.selecttranslate

import com.nikcapko.memo.core.ui.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.test.InstantExecutorExtension
import com.nikcapko.memo.core.test.TestDispatcherProvider
import com.nikcapko.memo.presentation.domain.SelectTranslateInteractor
import com.nikcapko.memo.presentation.games.selecttranslate.SelectTranslateViewModel
import com.nikcapko.memo.presentation.navigation.RootNavigator
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [SelectTranslateViewModel]
 */
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class SelectTranslateViewModelTest {

    private val selectTranslateInteractor = mockk<SelectTranslateInteractor>(relaxed = true)
    private val stateFlowWrapper = mockk<SelectTranslateStateFlowWrapper>(relaxed = true)
    private val rootNavigator = spyk<RootNavigator>()

    private val dispatcherProvider = TestDispatcherProvider()

    private lateinit var viewModel: SelectTranslateViewModel

    private var word1 = Word(
        id = 3928,
        word = "word",
        translate = "слово",
        frequency = 2.3f,
    )

    private var word2 = Word(
        id = 3929,
        word = "hallo",
        translate = "привет",
        frequency = 2.3f,
    )

    @Test
    fun `check load words for game`() = runTest {
        coEvery { selectTranslateInteractor.getWords() } returns listOf(word1)

        viewModel = createViewModel()
        viewModel.onViewFirstCreated()

        verify { stateFlowWrapper.update(DataLoadingViewModelState.LoadingState) }
        verify {
            stateFlowWrapper.update(
                DataLoadingViewModelState.LoadedState(
                    word1 to listOf(
                        word1.translate,
                    ),
                ),
            )
        }
    }

    @Test
    fun `check correct select translate word`() = runTest {
        coEvery { selectTranslateInteractor.getWords() } returns listOf(word1, word2)

        viewModel = createViewModel()
        viewModel.onTranslateClick(word1.translate)

//        coVerify { eventFlowWrapper.update(SelectTranslateEvent.SuccessAnimationEvent) }
    }

    @Test
    fun `check select translate word`() = runTest {
        coEvery { selectTranslateInteractor.getWords() } returns listOf(word1, word2)

        viewModel = createViewModel()
        viewModel.onTranslateClick(word2.translate)

//        coVerify { eventFlowWrapper.update(SelectTranslateEvent.ErrorAnimationEvent) }
    }

    @Test
    fun `check call onBackPressed`() {
        viewModel = createViewModel()
        viewModel.onBackPressed()

        verify { rootNavigator.back() }
    }

    private fun createViewModel(): SelectTranslateViewModel {
        return SelectTranslateViewModel(
            selectTranslateInteractor = selectTranslateInteractor,
            stateFlowWrapper = stateFlowWrapper,
            rootNavigator = rootNavigator,
            dispatcherProvider = dispatcherProvider,
        )
    }
}
