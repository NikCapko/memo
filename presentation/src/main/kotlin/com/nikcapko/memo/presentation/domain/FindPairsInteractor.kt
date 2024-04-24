package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.memo.core.common.data.Word
import com.nikcapko.memo.presentation.mapper.WordModelMapper
import javax.inject.Inject

private const val MAX_WORDS_COUNT_FIND_PAIRS = 5

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
