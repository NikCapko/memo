package com.nikcapko.memo.presentation.ui.words.details

import com.nikcapko.memo.core.test.InstantExecutorExtension
import com.nikcapko.memo.core.test.MainCoroutineDispatcherExtension
import com.nikcapko.memo.core.test.TestDispatcherProvider
import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.domain.repository.WordRepository
import com.nikcapko.memo.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.memo.domain.usecases.DeleteWordUseCase
import com.nikcapko.memo.domain.usecases.SaveWordUseCase
import com.nikcapko.memo.domain.usecases.WordListUseCase
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.screens.words.details.WordDetailsViewModel
import com.nikcapko.memo.presentation.screens.words.details.state.WordDetailsState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [WordDetailsViewModel]
 */
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, MainCoroutineDispatcherExtension::class)
internal class WordDetailsViewModelTest {

    private val rootNavigator = spyk<RootNavigator>()

    private val wordRepository = mockk<WordRepository>(relaxed = true)

    private val saveWordUseCase = SaveWordUseCase(wordRepository)
    private var deleteWordUseCase = DeleteWordUseCase(wordRepository)

    private lateinit var viewModel: WordDetailsViewModel

    private var word = WordModel(
        id = 3929,
        word = "expetenda",
        translate = "vituperatoribus",
        frequency = 2.3f,
    )

    @BeforeEach
    fun beforeEach() {
        viewModel = createViewModel()
    }

    @Test
    fun `check save word with correct params`() = runTest {
        viewModel.changeWordField("word")
        viewModel.changeTranslateField("слово")
        viewModel.onSaveWord()

        coVerify {
            saveWordUseCase.invoke(WordModel())
        }
        verify { rootNavigator.back() }
    }

    @Test
    fun `check delete word`() = runTest {
        every { stateFlowWrapper.value() } returns WordDetailsState(
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

    private fun createViewModel() = WordDetailsViewModel(
        word = null,
        saveWordUseCase = saveWordUseCase,
        deleteWordUseCase = deleteWordUseCase,
        rootNavigator = rootNavigator,
        dispatcherProvider = TestDispatcherProvider(),
    )
}
