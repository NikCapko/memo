package com.nikcapko.memo.usecases

import com.nikcapko.domain.repository.IWordRepository
import com.nikcapko.memo.data.Form
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.FormModelMapper
import com.nikcapko.memo.mapper.WordModelMapper
import javax.inject.Inject

class SaveWordUseCase @Inject constructor(
    private val wordRepository: IWordRepository,
    private val wordModelMapper: WordModelMapper,
    private val formModelMapper: FormModelMapper,
) {
    suspend fun saveWord(word: Word) {
        wordRepository.saveWord(wordModelMapper.mapToEntity(word))
    }

    suspend fun saveForm(form: Form) {
        wordRepository.saveForm(formModelMapper.mapToEntity(form))
    }
}
