package com.nikcapko.memo.usecases

import com.nikcapko.core.network.Resource
import com.nikcapko.domain.repository.IDictionaryRepository
import com.nikcapko.memo.data.Dictionary
import com.nikcapko.memo.mapper.DictionaryModelMapper
import javax.inject.Inject

class DictionaryListUseCase @Inject constructor(
    private val dictionaryRepository: IDictionaryRepository,
    private val dictionaryModelMapper: DictionaryModelMapper,
) {
    suspend fun getDictionaryList(): Resource<List<Dictionary>> {
        val response = dictionaryRepository.getDictionaryList()
        return if (response.status == Resource.Status.SUCCESS) {
            Resource.success(
                dictionaryModelMapper.mapFromEntityList(response.data ?: emptyList())
            )
        } else {
            Resource.error(
                response.message ?: "Упс, что-то пошло не так…"
            )
        }
    }
}
