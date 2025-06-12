package com.nikcapko.memo.presentation.ui.words.list

import com.nikcapko.memo.core.ui.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.test.InstantExecutorExtension
import com.nikcapko.memo.core.test.TestDispatcherProvider
import com.nikcapko.memo.presentation.domain.WordListInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.words.list.WordListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val MIN_WORDS_COUNT = 5

/**
 * Test for [WordListViewModel]
 */
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class WordListViewModelTest {

    private var wordListInteractor = mockk<WordListInteractor>(relaxed = true)
    private val stateFlowWrapper = mockk<WordListStateFlowWrapper>(relaxed = true)
    private var rootNavigator = spyk<RootNavigator>()

    private lateinit var viewModel: WordListViewModel

    private val word = Word(
        id = 3929,
        word = "expetenda",
        translate = "vituperatoribus",
        frequency = 2.3f,
    )

    @Test
    fun `check transfer data from wordListUseCase on call loadWords`() = runTest {
        coEvery { wordListInteractor.getWords() } returns listOf(word)

        viewModel = createViewModel()
        viewModel.loadWords()

        verify {
            stateFlowWrapper.update(DataLoadingViewModelState.LoadingState)
            stateFlowWrapper.update(DataLoadingViewModelState.LoadedState(listOf(word)))
        }
    }

    @Test
    fun `check open screen word detail on call onItemClick`() = runTest {
        every { stateFlowWrapper.value() } returns DataLoadingViewModelState.LoadedState(listOf(word))

        viewModel = createViewModel()
        viewModel.onItemClick(0)

        verify { rootNavigator.pushWordDetailScreen(word) }
    }

    @Test
    fun `check send speakOutChannel on call onEnableSound`() = runTest {
        every { stateFlowWrapper.value() } returns DataLoadingViewModelState.LoadedState(listOf(word))

        viewModel = createViewModel()
        viewModel.onEnableSound(0)

//        coVerify { eventFlowWrapper.update(WordListEvent.SpeakOutEvent(word.word)) }
    }

    @Test
    fun `check clear database on call clearDatabase`() = runTest {
        viewModel = createViewModel()
        viewModel.clearDatabase()

        coVerify { wordListInteractor.clearDataBase() }

        verifyOrder {
            stateFlowWrapper.update(DataLoadingViewModelState.LoadingState)
            stateFlowWrapper.update(DataLoadingViewModelState.LoadedState(emptyList<Word>()))
        }
    }

    @Test
    fun `check open screen word detail with null on call onAddWordClick`() {
        viewModel = createViewModel()
        viewModel.onAddWordClick()

        verify { rootNavigator.pushWordDetailScreen() }
    }

    @Test
    fun `check show need more words dialog on call openGamesScreen`() = runTest {
        every { stateFlowWrapper.value() } returns DataLoadingViewModelState.LoadedState(listOf(word))

        viewModel = createViewModel()
        viewModel.openGamesScreen()

//        coVerify { eventFlowWrapper.update(WordListEvent.ShowNeedMoreWordsEvent) }
    }

    @Test
    fun `check open screen games on call openGamesScreen`() {
        every { stateFlowWrapper.value() } returns DataLoadingViewModelState.LoadedState(
            List(
                MIN_WORDS_COUNT
            ) { word })

        viewModel = createViewModel()
        viewModel.openGamesScreen()

        verify { rootNavigator.pushGamesScreen() }
    }

    @Test
    fun `check send showClearDatabaseDialog on call onClearDatabaseClick`() = runTest {
        viewModel = createViewModel()
        viewModel.onClearDatabaseClick()

//        coVerify { eventFlowWrapper.update(WordListEvent.ShowClearDatabaseEvent) }
    }

    private fun createViewModel() = WordListViewModel(
        wordListInteractor = wordListInteractor,
        stateFlowWrapper = stateFlowWrapper,
        rootNavigator = rootNavigator,
        dispatcherProvider = TestDispatcherProvider(),
    )
}
