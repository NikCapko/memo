package com.nikcapko.memo.ui.words.list

import com.github.terrakok.cicerone.Router
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class WordListViewModelTest {

    private var router = mockk<Router>(relaxed = true)
    private var wordListUseCase = mockk<WordListUseCase>(relaxed = true)
    private var wordModelMapper = mockk<WordModelMapper>(relaxed = true)

    private lateinit var viewModel: WordListViewModel

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

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
        launch { viewModel.loadWords() }
        coVerify(exactly = 1) { wordListUseCase.invoke() }
    }

    @Test
    fun `check transfer data from wordListUseCase on call loadWords`() = runTest {
        val expected = emptyList<Word>()
        val result = viewModel.loadWords()
        coEvery { wordListUseCase.invoke() } returns emptyList()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun getSpeakOutChannel() {
    }

    @Test
    fun loadWords() {
    }

    @Test
    fun onItemClick() {
    }

    @Test
    fun onEnableSound() {
    }

    @Test
    fun onAddWordClick() {
    }

    @Test
    fun openGamesScreen() {
    }

    @ExperimentalCoroutinesApi
    @AfterAll
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }
}
