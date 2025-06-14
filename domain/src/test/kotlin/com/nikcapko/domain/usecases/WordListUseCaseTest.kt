package com.nikcapko.domain.usecases

import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.domain.repository.WordRepository
import com.nikcapko.memo.domain.usecases.WordListUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for [com.nikcapko.memo.domain.usecases.WordListUseCase]
 */
@ExperimentalCoroutinesApi
class WordListUseCaseTest {

    private var wordRepository: WordRepository = spyk()

    private var useCase = WordListUseCase(
        wordRepository = wordRepository,
    )

    @Test
    fun `check call wordRepository-deleteWord on invoke`() = runTest {
        useCase.invoke()
        coVerify(exactly = 1) { wordRepository.getWordsFromDB() }
    }

    @Test
    fun `returns data from wordRepository`() = runTest {
        val expected = listOf(mockk<WordModel>())
        coEvery { wordRepository.getWordsFromDB() } returns expected

        val result = useCase.invoke()

        Assertions.assertEquals(expected, result)
    }
}
