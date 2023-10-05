package com.nikcapko.domain.usecases

import com.nikcapko.domain.repository.WordRepository
import javax.inject.Inject

class DeleteWordUseCase @Inject constructor(
    private val wordRepository: WordRepository,
) {
    suspend operator fun invoke(id: String) {
        wordRepository.deleteWord(id)
    }
}
