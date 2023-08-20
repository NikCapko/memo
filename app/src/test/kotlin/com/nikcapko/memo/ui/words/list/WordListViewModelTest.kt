package com.nikcapko.memo.ui.words.list

import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import com.nikcapko.memo.ui.Screens
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

/**
 * Test for [WordListViewModel]
 */
@TestInstance(Lifecycle.PER_CLASS)
@ExperimentalCoroutinesApi
internal class WordListViewModelTest {

    private var router = spyk<Router>()
    private var wordListUseCase = mockk<WordListUseCase>(relaxed = true)
    private var wordModelMapper = spyk<WordModelMapper>()

    private lateinit var viewModel: WordListViewModel

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    private var word = Word(
        id = 3929,
        word = "expetenda",
        translation = "vituperatoribus",
        frequency = 2.3f
    )

    private var wordModel = WordModel(
        id = 3929,
        word = "expetenda",
        translation = "vituperatoribus",
        frequency = 2.3f
    )

    @ExperimentalCoroutinesApi
    @BeforeAll
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WordListViewModel(
            router = router,
            wordListUseCase = wordListUseCase,
            wordModelMapper = wordModelMapper,
        )
    }

    @Test
    fun `check use wordListUseCase on call loadWords`() = runTest {
        viewModel.loadWords()
        coVerify { wordListUseCase.invoke() }
    }

    @Test
    fun `check transfer data from wordListUseCase on call loadWords`() = runTest {
        val expected = DataLoadingViewModelState.LoadedState(listOf(word))
        coEvery { wordListUseCase.invoke() } returns listOf(wordModel)
        viewModel.loadWords()
        Assertions.assertEquals(expected, viewModel.state.first())
    }

    @Test
    fun `check open screen word detail on call onItemClick`() = runTest {
        val mockedWordDetailScreen = mockk<FragmentScreen>()
        mockkObject(Screens)
        every { Screens.wordDetailScreen(any()) } returns mockedWordDetailScreen
        coEvery { wordListUseCase.invoke() } returns listOf(wordModel)

        launch {
            viewModel.loadWords()
            viewModel.onItemClick(0)
        }.join()

        verify(exactly = 1) { router.navigateTo(mockedWordDetailScreen) }
    }

    @Test
    fun `check send speakOutChannel on call onEnableSound`() = runTest {
        val expected = word.word
        coEvery { wordListUseCase.invoke() } returns listOf(wordModel)
        viewModel.loadWords()

        viewModel.onEnableSound(0)

        Assertions.assertEquals(expected, viewModel.speakOutChannel.first())
    }

    @Test
    fun `check open screen word detail with null on call onAddWordClick`() {
        val mockedWordDetailScreen = mockk<FragmentScreen>()
        mockkObject(Screens)
        every { Screens.wordDetailScreen(null) } returns mockedWordDetailScreen

        viewModel.onAddWordClick()

        verify(exactly = 1) { router.navigateTo(mockedWordDetailScreen) }
    }

    @Test
    fun `check open screen games on call openGamesScreen`() {
        val mockedWordDetailScreen = mockk<FragmentScreen>()
        mockkObject(Screens)
        every { Screens.gamesScreen() } returns mockedWordDetailScreen

        viewModel.openGamesScreen()

        verify(exactly = 1) { router.navigateTo(mockedWordDetailScreen) }
    }

    @ExperimentalCoroutinesApi
    @AfterAll
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }
}
