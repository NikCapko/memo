package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.usecases.DeleteWordUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import com.nikcapko.memo.core.common.data.Word
import com.nikcapko.memo.presentation.mapper.WordModelMapper
import javax.inject.Inject

internal interface WordDetailsInteractor {
    suspend fun deleteWord(wordId: String)
    suspend fun saveWord(word: Word)
}

internal class WordDetailsInteractorImpl @Inject constructor(
    private val saveWordUseCase: SaveWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val wordModelMapper: WordModelMapper,
) : WordDetailsInteractor {

    override suspend fun deleteWord(wordId: String) {
        deleteWordUseCase(wordId)
    }

    override suspend fun saveWord(word: Word) {
        saveWordUseCase(wordModelMapper.mapToEntity(word))
    }
}
