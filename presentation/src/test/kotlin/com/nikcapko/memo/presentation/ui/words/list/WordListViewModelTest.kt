package com.nikcapko.memo.presentation.ui.words.list

import app.cash.turbine.test
import com.nikcapko.memo.core.test.InstantExecutorExtension
import com.nikcapko.memo.core.test.MainCoroutineDispatcherExtension
import com.nikcapko.memo.core.test.TestDispatcherProvider
import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.domain.repository.WordRepository
import com.nikcapko.memo.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.memo.domain.usecases.WordListUseCase
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.screens.words.list.WordListViewModel
import com.nikcapko.memo.presentation.screens.words.list.event.WordListEvent
import com.nikcapko.memo.presentation.screens.words.list.state.WordListState
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [WordListViewModel]
 */
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, MainCoroutineDispatcherExtension::class)
internal class WordListViewModelTest {

    private val wordRepository = mockk<WordRepository>(relaxed = true)

    private val wordListUseCase = WordListUseCase(wordRepository)
    private var clearDatabaseUseCase = ClearDatabaseUseCase(wordRepository)

    private var rootNavigator = spyk<RootNavigator>()

    private lateinit var viewModel: WordListViewModel

    @BeforeEach
    fun beforeEach() {
        viewModel = createViewModel()
    }

    private val word = WordModel(
        id = 3929,
        word = "expetenda",
        translate = "vituperatoribus",
        frequency = 2.3f,
    )

    @Test
    fun `check transfer data from wordListUseCase on call loadWords`() = runTest {
        coEvery { wordRepository.getWordsFromDB() } returns listOf(word)

        viewModel.state.test {
            viewModel.loadWords()

            coVerify { wordListUseCase() }

            awaitItem() shouldBe WordListState.None
            awaitItem() shouldBe WordListState.Loading
            awaitItem() shouldBe WordListState.Success(listOf(word))
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `check open screen word detail on call onItemClick`() = runTest {
        coEvery { wordRepository.getWordsFromDB() } returns listOf(word)

        viewModel.loadWords()

        viewModel.onItemClick(0)

        verify { rootNavigator.pushWordDetailScreen(word) }
    }

    @Test
    fun `check send speakOutChannel on call onEnableSound`() = runTest {
        coEvery { wordRepository.getWordsFromDB() } returns listOf(word)

        viewModel.loadWords()

        viewModel.eventFlow.test {
            viewModel.onEnableSound(0)

            awaitItem() shouldBe WordListEvent.SpeakOutEvent(word.word)
        }
    }

    @Test
    fun `check clear database on call clearDatabase`() = runTest {
        viewModel.clearDatabase()

        coVerify { clearDatabaseUseCase.invoke() }
    }

    @Test
    fun `check open screen word detail with null on call onAddWordClick`() {
        viewModel.onAddWordClick()

        verify { rootNavigator.pushWordDetailScreen() }
    }

    @Test
    fun `check show need more words dialog on call openGamesScreen`() = runTest {
        coEvery { wordRepository.getWordsFromDB() } returns listOf()

        viewModel.loadWords()

        viewModel.eventFlow.test {
            viewModel.openGamesScreen()

            awaitItem() shouldBe WordListEvent.ShowNeedMoreWordsEvent
        }
    }

    @Test
    fun `check open screen games on call openGamesScreen`() {
        coEvery { wordRepository.getWordsFromDB() } returns List(10) { word }

        viewModel.loadWords()

        viewModel.openGamesScreen()

        verify { rootNavigator.pushGamesScreen() }
    }

    @Test
    fun `check send showClearDatabaseDialog on call onClearDatabaseClick`() = runTest {
        viewModel.eventFlow.test {
            viewModel.onClearDatabaseClick()

            awaitItem() shouldBe WordListEvent.ShowClearDatabaseEvent
        }
    }

    private fun createViewModel() = WordListViewModel(
        clearDatabaseUseCase = clearDatabaseUseCase,
        wordListUseCase = wordListUseCase,
        rootNavigator = rootNavigator,
        dispatcherProvider = TestDispatcherProvider(),
    )
}
