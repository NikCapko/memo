package com.nikcapko.memo.domain

import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import javax.inject.Inject

internal const val MAX_WORDS_COUNT_SELECT_TRANSLATE = 5

internal interface SelectTranslateInteractor {
    suspend fun getWords(): List<Word>
    suspend fun saveWord(word: Word)
}

internal class SelectTranslateInteractorImpl @Inject constructor(
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val saveWordUseCase: SaveWordUseCase,
    private val wordModelMapper: WordModelMapper,
) : SelectTranslateInteractor {

    override suspend fun getWords(): List<Word> {
        return wordModelMapper.mapFromEntityList(
            gameWordsLimitUseCase(
                MAX_WORDS_COUNT_SELECT_TRANSLATE,
            ),
        )
    }

    override suspend fun saveWord(word: Word) {
        saveWordUseCase(wordModelMapper.mapToEntity(word))
    }
}
