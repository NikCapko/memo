package com.nikcapko.memo.presentation.ui.words.details

import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.test.InstantExecutorExtension
import com.nikcapko.memo.core.test.TestDispatcherProvider
import com.nikcapko.memo.presentation.domain.WordDetailsInteractor
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.words.details.WordDetailsStateFlowWrapper
import com.nikcapko.memo.presentation.words.details.WordDetailsViewModel
import com.nikcapko.memo.presentation.words.details.WordDetailsViewState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [WordDetailsViewModel]
 */
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class WordDetailsViewModelTest {

    private val wordDetailsInteractor = mockk<WordDetailsInteractor>(relaxed = true)
    private val stateFlowWrapper = mockk<WordDetailsStateFlowWrapper>(relaxed = true)
    private val rootNavigator = spyk<RootNavigator>()

    private lateinit var viewModel: WordDetailsViewModel

    private var word = Word(
        id = 3929,
        word = "expetenda",
        translation = "vituperatoribus",
        frequency = 2.3f,
    )

    @Test
    fun `check save word with correct params`() = runTest {
        viewModel = createViewModel()
        viewModel.onSaveWord("word", "слово")

        coVerify {
            wordDetailsInteractor.saveWord(any())
//            eventFlowWrapper.update(WordDetailsEvent.CloseScreenEvent)
        }
        verify { rootNavigator.back() }
    }

    @Test
    fun `check delete word`() = runTest {
        every { stateFlowWrapper.value() } returns WordDetailsViewState(
            word = word,
            showProgressDialog = false,
            enableSaveButton = false,
        )

        viewModel = createViewModel()
        viewModel.onDeleteWord()

        coVerify {
            wordDetailsInteractor.deleteWord(word.id.toString())
//            eventFlowWrapper.update(WordDetailsEvent.CloseScreenEvent)
        }
        verify { rootNavigator.back() }
    }

    @Test
    fun `check not enable save button on empty word and empty translate`() = runTest {
        viewModel = createViewModel()
        viewModel.changeWordField("")
        viewModel.changeTranslateField("")

        Assertions.assertEquals(
            false,
            viewModel.enableSaveButtonState.first(),
        )
    }

    @Test
    fun `check not enable save button on empty word and non empty translate`() = runTest {
        viewModel = createViewModel()
        viewModel.changeWordField("")
        viewModel.changeTranslateField("translate")

        Assertions.assertEquals(
            false,
            viewModel.enableSaveButtonState.first(),
        )
    }

    @Test
    fun `check not enable save button on non empty word and empty translate`() = runTest {
        viewModel = createViewModel()
        viewModel.changeWordField("word")
        viewModel.changeTranslateField("")

        Assertions.assertEquals(
            false,
            viewModel.enableSaveButtonState.first(),
        )
    }

    @Test
    fun `check enable save button on non empty word and non empty translate`() = runTest {
        viewModel = createViewModel()
        viewModel.changeWordField("word")
        viewModel.changeTranslateField("translate")

        Assertions.assertEquals(
            true,
            viewModel.enableSaveButtonState.first(),
        )
    }

    private fun createViewModel() = WordDetailsViewModel(
        word = null,
        wordDetailsInteractor = wordDetailsInteractor,
        stateFlowWrapper = stateFlowWrapper,
        rootNavigator = rootNavigator,
        dispatcherProvider = TestDispatcherProvider(),
    )
}
