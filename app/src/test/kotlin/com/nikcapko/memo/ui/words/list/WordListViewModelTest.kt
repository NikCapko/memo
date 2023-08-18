package com.nikcapko.memo.ui.words.list

import com.github.terrakok.cicerone.Router
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain

/**
 * Test for [WordListViewModel]
 */
@ExperimentalCoroutinesApi
class WordListViewModelTest : FunSpec({

    val router = mockk<Router>(relaxed = true)
    val wordListUseCase = mockk<WordListUseCase>(relaxed = true)
    val wordModelMapper = spyk<WordModelMapper>()

    lateinit var viewModel: WordListViewModel

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
        viewModel = WordListViewModel(
            router = router,
            wordListUseCase = wordListUseCase,
            wordModelMapper = wordModelMapper,
        )
    }

    test("check use wordListUseCase on call loadWords")
        .config(coroutineTestScope = true) {
            viewModel.loadWords()
            coVerify(exactly = 1) { wordListUseCase.invoke() }
        }

    test("check transfer data from wordListUseCase on call loadWords")
        .config(coroutineTestScope = true) {
            val expected = DataLoadingViewModelState.LoadedState<List<Word>>(emptyList())
            viewModel.loadWords()
            coEvery { wordListUseCase.invoke() } returns emptyList()
            viewModel.state.value shouldBe expected
        }
})
