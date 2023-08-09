package com.nikcapko.domain.usecases

import com.nikcapko.domain.repository.WordRepository
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Test for [GameWordsLimitUseCase]
 */
@ExperimentalCoroutinesApi
class GameWordsLimitUseCaseTest {

    private var wordRepository: WordRepository = spyk()

    private var useCase = GameWordsLimitUseCase(
        wordRepository = wordRepository
    )

    @Test
    fun `check call wordRepository-getWordsForGameByLimit on invoke`() = runTest {
        useCase.invoke(1)
        coVerify(exactly = 1) { wordRepository.getWordsForGameByLimit(1) }
    }
}
