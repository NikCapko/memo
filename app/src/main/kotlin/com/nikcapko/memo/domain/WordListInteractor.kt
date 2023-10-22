package com.nikcapko.memo.domain

import com.nikcapko.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.domain.usecases.WordListUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import javax.inject.Inject

internal interface WordListInteractor {
    suspend fun getWords(): List<Word>
    suspend fun clearDataBase()
}

internal class WordListInteractorImpl @Inject constructor(
    private val wordListUseCase: WordListUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
    private val wordModelMapper: WordModelMapper,
) : WordListInteractor {

    override suspend fun getWords(): List<Word> {
        val words = wordListUseCase()
        return wordModelMapper.mapFromEntityList(words)
    }

    override suspend fun clearDataBase() {
        clearDatabaseUseCase.invoke()
    }
}
