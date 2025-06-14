package com.nikcapko.domain.usecases

import com.nikcapko.memo.domain.repository.WordRepository
import com.nikcapko.memo.domain.usecases.GameWordsLimitUseCase
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Test for [com.nikcapko.memo.domain.usecases.GameWordsLimitUseCase]
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
