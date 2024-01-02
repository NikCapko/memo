package com.nikcapko.memo.ui.words.details

import com.nikcapko.memo.InstantExecutorExtension
import com.nikcapko.memo.TestDispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.WordDetailsInteractor
import com.nikcapko.memo.navigation.Navigator
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
    private val eventFlowWrapper = mockk<WordDetailsEventFlowWrapper>(relaxed = true)
    private val navigator = spyk<Navigator>()

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
            eventFlowWrapper.update(WordDetailsEvent.CloseScreenEvent)
        }
        verify { navigator.back() }
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
            eventFlowWrapper.update(WordDetailsEvent.CloseScreenEvent)
        }
        verify { navigator.back() }
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
        wordDetailsInteractor = wordDetailsInteractor,
        stateFlowWrapper = stateFlowWrapper,
        eventFlowWrapper = eventFlowWrapper,
        navigator = navigator,
        dispatcherProvider = TestDispatcherProvider(),
    )
}
