package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.usecases.GameWordsLimitUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import com.nikcapko.memo.core.common.converter.convert
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.presentation.mapper.WordModelListConverter
import com.nikcapko.memo.presentation.mapper.WordToWordModelConverter
import javax.inject.Inject

internal const val MAX_WORDS_COUNT_SELECT_TRANSLATE = 5

internal interface SelectTranslateInteractor {
    suspend fun getWords(): List<Word>
    suspend fun saveWord(word: Word)
}

internal class SelectTranslateInteractorImpl @Inject constructor(
    private val gameWordsLimitUseCase: GameWordsLimitUseCase,
    private val saveWordUseCase: SaveWordUseCase,
    private val wordToWordModelConverter: WordToWordModelConverter,
    private val wordModelListConverter: WordModelListConverter,
) : SelectTranslateInteractor {

    override suspend fun getWords(): List<Word> {
        return gameWordsLimitUseCase
            .invoke(MAX_WORDS_COUNT_SELECT_TRANSLATE)
            .convert(wordModelListConverter)
    }

    override suspend fun saveWord(word: Word) {
        saveWordUseCase(word.convert(wordToWordModelConverter))
    }
}
