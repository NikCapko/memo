package com.nikcapko.domain.usecases

import com.nikcapko.domain.repository.WordRepository
import javax.inject.Inject

class ClearDatabaseUseCase @Inject constructor(
    private val wordRepository: WordRepository,
) {
    suspend operator fun invoke() {
        wordRepository.clearDatabase()
    }
}
