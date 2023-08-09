package com.nikcapko.domain.usecases

import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.repository.WordRepository
import javax.inject.Inject

class WordListUseCase @Inject constructor(
    private val wordRepository: WordRepository,
) {
    suspend operator fun invoke(): List<WordModel> {
        return wordRepository.getWordsFromDB()
    }
}
