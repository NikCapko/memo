package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import javax.inject.Inject

internal const val MAX_WORDS_COUNT_SELECT_TRANSLATE = 5

internal interface SelectTranslateInteractor {
    suspend fun getWords(): List<WordModel>
    suspend fun saveWord(word: WordModel)
}

internal class SelectTranslateInteractorImpl @Inject constructor(
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val saveWordUseCase: SaveWordUseCase,
) : SelectTranslateInteractor {

    override suspend fun getWords(): List<WordModel> {
        return gameWordsLimitUseCase
            .invoke(MAX_WORDS_COUNT_SELECT_TRANSLATE)
    }

    override suspend fun saveWord(word: WordModel) {
        saveWordUseCase(word)
    }
}
