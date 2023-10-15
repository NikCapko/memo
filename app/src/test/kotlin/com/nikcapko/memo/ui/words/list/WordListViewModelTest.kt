package com.nikcapko.memo.ui.words.list

import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.InstantExecutorExtension
import com.nikcapko.memo.TestDispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
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
import ru.ar2code.mutableliveevent.EventArgs

/**
 * Test for [WordListViewModel]
 */
@TestInstance(Lifecycle.PER_CLASS)
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class WordListViewModelTest {

    private var navigator = spyk<Navigator>()
    private var wordListUseCase = mockk<WordListUseCase>(relaxed = true)
    private var clearDatabaseUseCase = mockk<ClearDatabaseUseCase>(relaxed = true)
    private var wordModelMapper = spyk<WordModelMapper>()

    private lateinit var viewModel: WordListViewModel

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

    @BeforeAll
    fun setupDispatcher() {
        viewModel = WordListViewModel(
            navigator = navigator,
            wordListUseCase = wordListUseCase,
            clearDatabaseUseCase = clearDatabaseUseCase,
            wordModelMapper = wordModelMapper,
            dispatcherProvider = TestDispatcherProvider()
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
        coEvery { wordListUseCase.invoke() } returns listOf(wordModel)

        viewModel.loadWords()
        viewModel.onItemClick(0)

        verify(exactly = 1) { navigator.pushWordDetailScreen(word) }
    }

    @Test
    fun `check send speakOutChannel on call onEnableSound`() {
        val expected = EventArgs(word.word)
        coEvery { wordListUseCase.invoke() } returns listOf(wordModel)

        viewModel.loadWords()
        viewModel.onEnableSound(0)

        Assertions.assertEquals(expected.data, viewModel.speakOutEvent.value?.data)
    }

    @Test
    fun `check open screen word detail with null on call onAddWordClick`() {
        viewModel.onAddWordClick()

        verify(exactly = 1) { navigator.pushWordDetailScreen() }
    }

    @Test
    fun `check open screen games on call openGamesScreen`() {
        viewModel.openGamesScreen()

        verify(exactly = 1) { navigator.pushGamesScreen() }
    }

    @Test
    fun `check send showClearDatabaseDialog on call onClearDatabaseClick`() {
        viewModel.onClearDatabaseClick()

        Assertions.assertNotNull(viewModel.showClearDatabaseDialog.value)
    }
}
