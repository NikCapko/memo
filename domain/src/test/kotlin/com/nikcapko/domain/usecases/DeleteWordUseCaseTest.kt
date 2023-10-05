package com.nikcapko.domain.usecases

import com.nikcapko.domain.repository.WordRepository
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Test for [DeleteWordUseCase]
 */
@ExperimentalCoroutinesApi
class DeleteWordUseCaseTest {

    private var wordRepository: WordRepository = spyk()

    private var useCase = DeleteWordUseCase(
        wordRepository = wordRepository
    )

    @Test
    fun `check call wordRepository-deleteWord on invoke`() = runTest {
        useCase.invoke("1")
        coVerify(exactly = 1) { wordRepository.deleteWord("1") }
    }
}
