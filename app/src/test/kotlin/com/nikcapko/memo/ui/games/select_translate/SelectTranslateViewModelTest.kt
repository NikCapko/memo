package com.nikcapko.memo.ui.games.select_translate

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.InstantExecutorExtension
import com.nikcapko.memo.TestDispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.SelectTranslateInteractor
import com.nikcapko.memo.navigation.Navigator
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [SelectTranslateViewModel]
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class SelectTranslateViewModelTest {
    private val selectTranslateInteractor = mockk<SelectTranslateInteractor>(relaxed = true)
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

        viewModel = SelectTranslateViewModel(
            selectTranslateInteractor = selectTranslateInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )

        Assertions.assertThat(viewModel.state.first())
            .isInstanceOf(
                DataLoadingViewModelState.LoadedState::class.java,
            )

        val data =
            (viewModel.state.first() as DataLoadingViewModelState.LoadedState<Pair<Word, List<String>>>).data

        Assertions.assertThat(data?.first).isEqualTo(word1)
        Assertions.assertThat(data?.second)
            .containsAll(listOf(word1.translation, word2.translation))
    }

    @Test
    fun `check correct select translate word`() = runTest {
        coEvery { selectTranslateInteractor.getWords() } returns listOf(word1, word2)

        viewModel = SelectTranslateViewModel(
            selectTranslateInteractor = selectTranslateInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )

        viewModel.onTranslateClick(word1.translation)

        org.junit.jupiter.api.Assertions.assertEquals(
            SelectTranslateEvent.SuccessAnimationEvent(true),
            viewModel.successAnimationEvent.value,
        )
    }

    @Test
    fun `check select translate word`() = runTest {
        coEvery { selectTranslateInteractor.getWords() } returns listOf(word1, word2)

        viewModel = SelectTranslateViewModel(
            selectTranslateInteractor = selectTranslateInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )

        viewModel.onTranslateClick(word2.translation)

        org.junit.jupiter.api.Assertions.assertEquals(
            SelectTranslateEvent.SuccessAnimationEvent(false),
            viewModel.successAnimationEvent.value,
        )
    }

    @Test
    fun `check call onBackPressed`() {
        viewModel = SelectTranslateViewModel(
            selectTranslateInteractor = selectTranslateInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )
        viewModel.onBackPressed()

        verify { navigator.back() }
    }
}
