package com.nikcapko.memo.presentation.ui.games.findpairs

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.test.InstantExecutorExtension
import com.nikcapko.memo.core.test.TestDispatcherProvider
import com.nikcapko.memo.presentation.domain.FindPairsInteractor
import com.nikcapko.memo.presentation.games.findpairs.FindPairsState
import com.nikcapko.memo.presentation.games.findpairs.FindPairsStateFlowWrapper
import com.nikcapko.memo.presentation.games.findpairs.FindPairsViewModel
import com.nikcapko.memo.presentation.navigation.RootNavigator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [FindPairsViewModel]
 */
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class FindPairsViewModelTest {

    private val findPairsInteractor = mockk<FindPairsInteractor>(relaxed = true)
    private val rootNavigator = spyk<RootNavigator>()
    private val stateFlowWrapper = mockk<FindPairsStateFlowWrapper>(relaxed = true)
    private val dispatcherProvider = TestDispatcherProvider()

    private lateinit var viewModel: FindPairsViewModel

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
        val stateSlot = mutableListOf<(FindPairsState) -> FindPairsState>()
        every { stateFlowWrapper.update(capture(stateSlot)) } returns Unit
        coEvery { findPairsInteractor.getWordsForGame() } returns listOf(word1, word2)

        viewModel = createViewModel()
        viewModel.onViewFirstCreated()

        val noneState = FindPairsState(
            dataLoadingViewModelState = DataLoadingViewModelState.NoneState,
            wordList = emptyList(),
            translateList = emptyList(),
            wordsCount = 0,
        )
        val loadingState = FindPairsState(
            dataLoadingViewModelState = DataLoadingViewModelState.LoadingState,
            wordList = emptyList(),
            translateList = emptyList(),
            wordsCount = 0,
        )
        val loadedState = FindPairsState(
            dataLoadingViewModelState = DataLoadingViewModelState.LoadedState(
                listOf(
                    word1,
                    word2,
                ),
            ),
            wordList = listOf(word1.word, word2.word),
            translateList = listOf(word1.translation, word2.translation),
            wordsCount = 0,
        )

        coVerify { findPairsInteractor.getWordsForGame() }
        verify { stateFlowWrapper.update(noneState) }
        Assertions.assertEquals(stateSlot.size, 2)
        assertFindPairsState(stateSlot[0].invoke(noneState), loadingState)
        assertFindPairsState(stateSlot[1].invoke(loadingState), loadedState)
    }

    private fun assertFindPairsState(expected: FindPairsState, actual: FindPairsState) {
        SoftAssertions().apply {
            assertThat(expected.dataLoadingViewModelState).isEqualTo(actual.dataLoadingViewModelState)
            assertThat(expected.wordList).containsExactlyInAnyOrderElementsOf(actual.wordList)
            assertThat(expected.translateList).containsExactlyInAnyOrderElementsOf(actual.translateList)
            assertThat(expected.wordsCount).isEqualTo(actual.wordsCount)
        }.assertAll()
    }

    @Test
    fun `check find incorrect pair first word - second translate`() = runTest {
        every { stateFlowWrapper.value() } returns
                FindPairsState(
                    DataLoadingViewModelState.LoadedState(listOf(word1, word2)),
                    wordList = listOf(word1.word),
                    translateList = listOf(word1.translation),
                    wordsCount = 0,
                )

        viewModel = createViewModel()
        viewModel.onFindPair(word1.word, word2.translation)

        // coVerify { eventFlowWrapper.update(FindPairsEvent.FindPairResultEvent(false)) }
    }

    @Test
    fun `check find incorrect pair second word - first translate`() = runTest {
        coEvery { stateFlowWrapper.value() } returns FindPairsState(
            dataLoadingViewModelState = DataLoadingViewModelState.LoadedState(
                listOf(word1, word2),
            ),
            wordList = emptyList(),
            translateList = emptyList(),
            wordsCount = 0,
        )

        viewModel = createViewModel()
        viewModel.onFindPair(word2.word, word1.translation)

        // coVerify { eventFlowWrapper.update(FindPairsEvent.FindPairResultEvent(false)) }
    }

    @Test
    fun `check find correct pair first word - first translate`() = runTest {
        coEvery { stateFlowWrapper.value() } returns FindPairsState(
            dataLoadingViewModelState = DataLoadingViewModelState.LoadedState(
                listOf(word1, word2),
            ),
            wordList = emptyList(),
            translateList = emptyList(),
            wordsCount = 0,
        )

        viewModel = createViewModel()
        viewModel.onFindPair(word1.word, word1.translation)

        stateFlowWrapper.update { it.copy(wordsCount = 1) }
//        coVerify { eventFlowWrapper.update(FindPairsEvent.FindPairResultEvent(true)) }
    }

    @Test
    fun `check call onBackPressed`() {
        viewModel = createViewModel()
        viewModel.onBackPressed()

        verify { rootNavigator.back() }
    }

    private fun createViewModel(): FindPairsViewModel {
        return FindPairsViewModel(
            findPairsInteractor = findPairsInteractor,
            stateFlowWrapper = stateFlowWrapper,
            rootNavigator = rootNavigator,
            dispatcherProvider = dispatcherProvider,
        )
    }
}
