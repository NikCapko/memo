package com.nikcapko.memo.usecases

import com.nikcapko.domain.repository.IWordRepository
import javax.inject.Inject

class ClearDatabaseUseCase @Inject constructor(
    private val wordRepository: IWordRepository,
) {
    suspend fun clearDatabase() {
        wordRepository.clearDatabase()
    }
}
