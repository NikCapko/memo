package com.nikcapko.memo.domain.usecases

import com.nikcapko.memo.domain.repository.WordRepository
import javax.inject.Inject

class ClearDatabaseUseCase @Inject constructor(
    private val wordRepository: WordRepository,
) {
    suspend operator fun invoke() {
        wordRepository.clearDatabase()
    }
}
