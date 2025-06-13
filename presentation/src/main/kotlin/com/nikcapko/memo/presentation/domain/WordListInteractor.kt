package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.usecases.ClearDatabaseUseCase
import com.nikcapko.domain.usecases.WordListUseCase
import javax.inject.Inject

internal interface WordListInteractor {
    suspend fun getWords(): List<WordModel>
    suspend fun clearDataBase()
}

internal class WordListInteractorImpl @Inject constructor(
    private val wordListUseCase: WordListUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
) : WordListInteractor {

    override suspend fun getWords(): List<WordModel> {
        return wordListUseCase()
    }

    override suspend fun clearDataBase() {
        clearDatabaseUseCase.invoke()
    }
}
