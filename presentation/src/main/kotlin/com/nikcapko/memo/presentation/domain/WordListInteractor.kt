package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.core.common.converter.convert
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.presentation.mapper.WordModelListConverter
import javax.inject.Inject

internal interface WordListInteractor {
    suspend fun getWords(): List<Word>
    suspend fun clearDataBase()
}

internal class WordListInteractorImpl @Inject constructor(
    private val wordListUseCase: WordListUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
    private val wordModelListConverter: WordModelListConverter,
) : WordListInteractor {

    override suspend fun getWords(): List<Word> {
        return wordListUseCase
            .invoke()
            .convert(wordModelListConverter)
    }

    override suspend fun clearDataBase() {
        clearDatabaseUseCase.invoke()
    }
}
