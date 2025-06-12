package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import javax.inject.Inject

private const val MAX_WORDS_COUNT_FIND_PAIRS = 5

internal interface FindPairsInteractor {
    suspend fun getWordsForGame(): List<WordModel>
}

internal class FindPairsInteractorImpl @Inject constructor(
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
) : FindPairsInteractor {

    override suspend fun getWordsForGame(): List<WordModel> {
        return gameWordsLimitUseCase(MAX_WORDS_COUNT_FIND_PAIRS)
    }
}
