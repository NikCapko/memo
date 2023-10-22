package com.nikcapko.memo.domain

import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.memo.data.MAX_WORDS_COUNT_FIND_PAIRS
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import javax.inject.Inject

internal interface FindPairsInteractor {
    suspend fun getWordsForGame(): List<Word>
}

internal class FindPairsInteractorImpl @Inject constructor(
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val wordModelMapper: WordModelMapper,
) : FindPairsInteractor {

    override suspend fun getWordsForGame(): List<Word> {
        return wordModelMapper.mapFromEntityList(
            gameWordsLimitUseCase(MAX_WORDS_COUNT_FIND_PAIRS),
        )
    }
}
