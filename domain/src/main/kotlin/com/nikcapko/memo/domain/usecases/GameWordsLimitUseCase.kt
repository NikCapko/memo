package com.nikcapko.memo.domain.usecases

import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.domain.repository.WordRepository
import javax.inject.Inject

class GameWordsLimitUseCase @Inject constructor(
    private val wordRepository: WordRepository,
) {
    suspend operator fun invoke(limit: Int): List<WordModel> {
        return wordRepository.getWordsForGameByLimit(limit)
    }
}
