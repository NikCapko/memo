package com.nikcapko.memo.usecases

import com.nikcapko.core.network.Resource
import com.nikcapko.domain.repository.IDictionaryRepository
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
import javax.inject.Inject

class DictionaryUseCase @Inject constructor(
    private val dictionaryRepository: IDictionaryRepository,
    private val wordModelMapper: WordModelMapper,
) {
    suspend fun getDictionary(id: String): Resource<List<Word>> {
        val response = dictionaryRepository.getDictionary(id)
        return if (response.status == Resource.Status.SUCCESS) {
            Resource.success(
                wordModelMapper.mapFromEntityList(response.data ?: emptyList())
            )
        } else {
            Resource.error(
                response.message ?: "Упс, что-то пошло не так…"
            )
        }
    }
}
