package com.nikcapko.memo.ui.words.list

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.InstantExecutorExtension
import com.nikcapko.memo.TestDispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.WordListInteractor
import com.nikcapko.memo.navigation.Navigator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith

private const val MAX_WORDS_COUNT_SELECT_TRANSLATE = 5

/**
 * Test for [WordListViewModel]
 */
@TestInstance(Lifecycle.PER_CLASS)
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class WordListViewModelTest {

    private var navigator = spyk<Navigator>()
    private var wordListInteractor = mockk<WordListInteractor>(relaxed = true)

    private lateinit var viewModel: WordListViewModel

    private var word = Word(
        id = 3929,
        word = "expetenda",
        translation = "vituperatoribus",
        frequency = 2.3f,
    )

    @BeforeAll
    fun setupDispatcher() {
        viewModel = WordListViewModel(
            navigator = navigator,
            wordListInteractor = wordListInteractor,
            dispatcherProvider = TestDispatcherProvider(),
        )
    }

    @Test
    fun `check use wordListUseCase on call loadWords`() = runTest {
        viewModel.loadWords()

        coVerify { wordListInteractor.getWords() }
    }

    @Test
    fun `check transfer data from wordListUseCase on call loadWords`() = runTest {
        val expected = DataLoadingViewModelState.LoadedState(listOf(word))
        coEvery { wordListInteractor.getWords() } returns listOf(word)

        viewModel.loadWords()

        Assertions.assertEquals(expected, viewModel.state.first())
    }

    @Test
    fun `check open screen word detail on call onItemClick`() = runTest {
        coEvery { wordListInteractor.getWords() } returns listOf(word)

        viewModel.loadWords()
        viewModel.onItemClick(0)

        verify(exactly = 1) { navigator.pushWordDetailScreen(word) }
    }

    @Test
    fun `check send speakOutChannel on call onEnableSound`() {
        val expected = WordListEvent.SpeakOutEvent(word.word)
        coEvery { wordListInteractor.getWords() } returns listOf(word)

        viewModel.loadWords()
        viewModel.onEnableSound(0)

        Assertions.assertEquals(expected, viewModel.speakOutEvent.value)
    }

    @Test
    fun `check clear database on call clearDatabase`() = runTest {
        viewModel.clearDatabase()

        coVerify(exactly = 1) { wordListInteractor.clearDataBase() }
        Assertions.assertEquals(
            DataLoadingViewModelState.LoadedState(emptyList<Word>()),
            viewModel.state.first(),
        )
    }

    @Test
    fun `check open screen word detail with null on call onAddWordClick`() {
        viewModel.onAddWordClick()

        verify(exactly = 1) { navigator.pushWordDetailScreen() }
    }

    @Test
    fun `check show need more words dialog on call openGamesScreen`() {
        val expected = WordListEvent.ShowNeedMoreWordsEvent
        coEvery { wordListInteractor.getWords() } returns emptyList()

        viewModel.loadWords()
        viewModel.openGamesScreen()

        Assertions.assertEquals(expected, viewModel.showNeedMoreWordsDialog.value)
    }

    @Test
    fun `check open screen games on call openGamesScreen`() {
        coEvery { wordListInteractor.getWords() } returns List(MAX_WORDS_COUNT_SELECT_TRANSLATE) { word }

        viewModel.loadWords()
        viewModel.openGamesScreen()

        verify(exactly = 1) { navigator.pushGamesScreen() }
    }

    @Test
    fun `check send showClearDatabaseDialog on call onClearDatabaseClick`() {
        viewModel.onClearDatabaseClick()

        Assertions.assertNotNull(viewModel.showClearDatabaseDialog.value)
    }
}
