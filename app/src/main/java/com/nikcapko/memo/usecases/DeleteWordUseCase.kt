package com.nikcapko.memo.usecases

import com.nikcapko.domain.repository.IWordRepository
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import javax.inject.Inject

class DeleteWordUseCase @Inject constructor(
    private val wordRepository: IWordRepository,
    private val wordModelMapper: WordModelMapper,
) {
    suspend fun deleteWord(word: Word) {
        wordRepository.deleteWord(wordModelMapper.mapToEntity(word))
    }
}
