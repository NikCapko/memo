package com.nikcapko.memo.ui.words.details

import com.nikcapko.memo.InstantExecutorExtension
import com.nikcapko.memo.TestDispatcherProvider
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.domain.WordDetailsInteractor
import com.nikcapko.memo.navigation.Navigator
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [WordDetailsViewModel]
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class WordDetailsViewModelTest {

    private val wordDetailsInteractor = mockk<WordDetailsInteractor>(relaxed = true)
    private val navigator = spyk<Navigator>()
    private val dispatcherProvider = TestDispatcherProvider()

    private lateinit var viewModel: WordDetailsViewModel

    private var word = Word(
        id = 3929,
        word = "expetenda",
        translation = "vituperatoribus",
        frequency = 2.3f,
    )

    @BeforeEach
    fun setupDispatcher() {
        viewModel = WordDetailsViewModel(
            wordDetailsInteractor = wordDetailsInteractor,
            navigator = navigator,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `check save word with correct params`() = runTest {
        viewModel.onSaveWord("word", "слово")

        coVerify(exactly = 1) { wordDetailsInteractor.saveWord(any()) }
        Assertions.assertEquals(WordDetailsEvent.CloseScreenEvent, viewModel.closeScreenEvent.value)
        verify(exactly = 1) { navigator.back() }
    }

    @Test
    fun `check delete word`() {
        viewModel.setArguments(word)
        viewModel.onDeleteWord()

        coVerify(exactly = 1) { wordDetailsInteractor.deleteWord(word.id.toString()) }
        Assertions.assertEquals(WordDetailsEvent.CloseScreenEvent, viewModel.closeScreenEvent.value)
        verify(exactly = 1) { navigator.back() }
    }

    @Test
    fun `check not enable save button on empty word and empty translate`() = runTest {
        viewModel.changeWordField("")
        viewModel.changeTranslateField("")

        Assertions.assertEquals(
            false,
            viewModel.enableSaveButtonState.first(),
        )
    }

    @Test
    fun `check not enable save button on empty word and non empty translate`() = runTest {
        viewModel.changeWordField("")
        viewModel.changeTranslateField("translate")

        Assertions.assertEquals(
            false,
            viewModel.enableSaveButtonState.first(),
        )
    }

    @Test
    fun `check not enable save button on non empty word and empty translate`() = runTest {
        viewModel.changeWordField("word")
        viewModel.changeTranslateField("")

        Assertions.assertEquals(
            false,
            viewModel.enableSaveButtonState.first(),
        )
    }

    @Test
    fun `check enable save button on non empty word and non empty translate`() = runTest {
        viewModel.changeWordField("word")
        viewModel.changeTranslateField("translate")

        Assertions.assertEquals(
            true,
            viewModel.enableSaveButtonState.first(),
        )
    }
}
