package com.nikcapko.memo.ui.games.find_pairs

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.InstantExecutorExtension
import com.nikcapko.memo.TestDispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.FindPairsInteractor
import com.nikcapko.memo.navigation.Navigator
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [FindPairsViewModel]
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class FindPairsViewModelTest {

    private val findPairsInteractor = mockk<FindPairsInteractor>(relaxed = true)
    private val navigator = spyk<Navigator>()
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
        coEvery { findPairsInteractor.getWordsForGame() } returns listOf(word1, word2)

        viewModel = FindPairsViewModel(
            findPairsInteractor = findPairsInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )

        assertThat(viewModel.state.first())
            .isInstanceOf(
                DataLoadingViewModelState.LoadedState::class.java,
            )

        val data =
            (viewModel.state.first() as DataLoadingViewModelState.LoadedState<Pair<List<String>, List<String>>>).data

        assertThat(data?.first).containsAll(listOf(word1.word, word2.word))
        assertThat(data?.second).containsAll(listOf(word1.translation, word2.translation))
    }

    @Test
    fun `check find incorrect pair first word - second translate`() {
        coEvery { findPairsInteractor.getWordsForGame() } returns listOf(word1, word2)

        viewModel = FindPairsViewModel(
            findPairsInteractor = findPairsInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )
        viewModel.onFindPair(word1.word, word2.translation)

        Assertions.assertEquals(
            FindPairsEvent.FindPairResultEvent(false),
            viewModel.findPairResultEvent.value,
        )
    }

    @Test
    fun `check find incorrect pair second word - first translate`() {
        coEvery { findPairsInteractor.getWordsForGame() } returns listOf(word1, word2)

        viewModel = FindPairsViewModel(
            findPairsInteractor = findPairsInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )
        viewModel.onFindPair(word2.word, word1.translation)

        Assertions.assertEquals(
            FindPairsEvent.FindPairResultEvent(false),
            viewModel.findPairResultEvent.value,
        )
    }

    @Test
    fun `check find correct pair first word - first translate`() {
        coEvery { findPairsInteractor.getWordsForGame() } returns listOf(word1, word2)

        viewModel = FindPairsViewModel(
            findPairsInteractor = findPairsInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )
        viewModel.onFindPair(word1.word, word1.translation)

        Assertions.assertEquals(
            FindPairsEvent.FindPairResultEvent(true),
            viewModel.findPairResultEvent.value,
        )
    }

    @Test
    fun `check call onBackPressed`() {
        viewModel = FindPairsViewModel(
            findPairsInteractor = findPairsInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )
        viewModel.onBackPressed()

        verify { navigator.back() }
    }
}
