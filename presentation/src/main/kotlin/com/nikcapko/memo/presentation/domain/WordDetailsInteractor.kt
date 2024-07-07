package com.nikcapko.memo.presentation.domain

import com.nikcapko.domain.usecases.DeleteWordUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import com.nikcapko.memo.core.common.converter.convert
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.presentation.mapper.WordToWordModelConverter
import javax.inject.Inject

internal interface WordDetailsInteractor {
    suspend fun deleteWord(wordId: String)
    suspend fun saveWord(word: Word)
}

internal class WordDetailsInteractorImpl @Inject constructor(
    private val saveWordUseCase: SaveWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val wordToWordModelConverter: WordToWordModelConverter,
) : WordDetailsInteractor {

    override suspend fun deleteWord(wordId: String) {
        deleteWordUseCase(wordId)
    }

    override suspend fun saveWord(word: Word) {
        saveWordUseCase(word.convert(wordToWordModelConverter))
    }
}
