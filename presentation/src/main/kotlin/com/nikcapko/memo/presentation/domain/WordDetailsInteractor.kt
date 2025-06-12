package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.usecases.DeleteWordUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import javax.inject.Inject

internal interface WordDetailsInteractor {
    suspend fun deleteWord(wordId: String)
    suspend fun saveWord(word: WordModel)
}

internal class WordDetailsInteractorImpl @Inject constructor(
    private val saveWordUseCase: SaveWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
) : WordDetailsInteractor {

    override suspend fun deleteWord(wordId: String) {
        deleteWordUseCase(wordId)
    }

    override suspend fun saveWord(word: WordModel) {
        saveWordUseCase(word)
    }
}
