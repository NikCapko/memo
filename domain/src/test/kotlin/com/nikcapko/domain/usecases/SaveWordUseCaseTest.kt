package com.nikcapko.domain.usecases

import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.repository.WordRepository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Test for [SaveWordUseCase]
 */
@ExperimentalCoroutinesApi
class SaveWordUseCaseTest {

    private var wordRepository: WordRepository = spyk()

    private var useCase = SaveWordUseCase(
        wordRepository = wordRepository
    )

    private val word = mockk<WordModel>()

    @Test
    fun `check call wordRepository-saveWord on invoke`() = runTest {
        useCase.invoke(word)
        coVerify(exactly = 1) { wordRepository.saveWord(word) }
    }
}
