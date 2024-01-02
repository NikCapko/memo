package com.nikcapko.memo.ui.games.select_translate

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.InstantExecutorExtension
import com.nikcapko.memo.TestDispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.SelectTranslateInteractor
import com.nikcapko.memo.navigation.Navigator
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val eventFlowWrapper = mockk<SelectTranslateEventFlowWrapper>(relaxed = true)
    private val navigator = spyk<Navigator>()

    private val dispatcherProvider = TestDispatcherProvider()

    private lateinit var viewModel: SelectTranslateViewModel

    private var word1 = Word(
        id = 3928,
        word = "word",
        translation = "слово",
        frequency = 2.3f,
    )

    private var word2 = Word(
        id = 3929,
        word = "hallo",
        translation = "привет",
        frequency = 2.3f,
    )

    @Test
    fun `check load words for game`() = runTest {
        coEvery { selectTranslateInteractor.getWords() } returns listOf(word1, word2)

        viewModel = createViewModel()

        verify { stateFlowWrapper.update(DataLoadingViewModelState.LoadingState) }
        verify {
            stateFlowWrapper.update(
                DataLoadingViewModelState.LoadedState(
                    word1 to listOf(
                        word1.translation,
                        word2.translation,
                    ),
                ),
            )
        }
    }

    @Test
    fun `check correct select translate word`() = runTest {
        coEvery { selectTranslateInteractor.getWords() } returns listOf(word1, word2)

        viewModel = createViewModel()
        viewModel.onTranslateClick(word1.translation)

        coVerify {
            eventFlowWrapper.update(SelectTranslateEvent.SuccessAnimationEvent(true))
        }
    }

    @Test
    fun `check select translate word`() = runTest {
        coEvery { selectTranslateInteractor.getWords() } returns listOf(word1, word2)

        viewModel = createViewModel()
        viewModel.onTranslateClick(word2.translation)

        coVerify {
            eventFlowWrapper.update(SelectTranslateEvent.SuccessAnimationEvent(false))
        }
    }

    @Test
    fun `check call onBackPressed`() {
        viewModel = createViewModel()
        viewModel.onBackPressed()

        verify { navigator.back() }
    }

    private fun createViewModel(): SelectTranslateViewModel {
        return SelectTranslateViewModel(
            selectTranslateInteractor = selectTranslateInteractor,
            stateFlowWrapper = stateFlowWrapper,
            eventFlowWrapper = eventFlowWrapper,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )
    }
}
