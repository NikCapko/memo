package com.nikcapko.memo.usecases

import com.nikcapko.domain.repository.IWordRepository
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import javax.inject.Inject

class GameWordsLimitUseCase @Inject constructor(
    private val wordRepository: IWordRepository,
    private val wordModelMapper: WordModelMapper,
) {
    suspend fun getWordList(limit: Int): List<Word> {
        return wordModelMapper.mapFromEntityList(wordRepository.getWordsForGameByLimit(limit))
    }
}
