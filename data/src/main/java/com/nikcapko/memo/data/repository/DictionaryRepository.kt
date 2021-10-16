package com.nikcapko.memo.data.repository

import com.nikcapko.core.network.Resource
import com.nikcapko.domain.model.DictionaryModel
import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.repository.IDictionaryRepository
import com.nikcapko.memo.data.network.ApiServiceImpl
import com.nikcapko.memo.data.network.mapper.DictionaryEntityMapper
import com.nikcapko.memo.data.network.mapper.WordEntityMapper
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    private val apiService: ApiServiceImpl,
    private val dictionaryEntityMapper: DictionaryEntityMapper,
    private val wordEntityMapper: WordEntityMapper,
) : IDictionaryRepository {

    override suspend fun getDictionaryList(): Resource<List<DictionaryModel>> {
        val response = apiService.getDictionaryList()
        return if (response.status == Resource.Status.SUCCESS) {
            Resource.success(
                dictionaryEntityMapper.mapFromEntityList(response.data ?: emptyList())
            )
        } else {
            Resource.error(
                response.message ?: "Упс, что-то пошло не так…"
            )
        }
    }

    override suspend fun getDictionary(id: String): Resource<List<WordModel>> {
        val response = apiService.getDictionary(id)
        return if (response.status == Resource.Status.SUCCESS) {
            Resource.success(
                wordEntityMapper.mapFromEntityList(response.data ?: emptyList())
            )
        } else {
            Resource.error(
                response.message ?: "Упс, что-то пошло не так…"
            )
        }
    }
}
