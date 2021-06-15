package com.nik.capko.memo.repository

import com.nik.capko.memo.base.network.Resource
import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.network.ApiServiceImpl
import com.nik.capko.memo.network.mapper.DictionaryEntityMapper
import com.nik.capko.memo.network.mapper.WordEntityMapper
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    api: ApiServiceImpl,
    private val dictionaryEntityMapper: DictionaryEntityMapper,
    private val wordEntityMapper: WordEntityMapper,
) {
    private val apiService = api

    suspend fun getDictionaryList(): Resource<List<Dictionary>> {
        val response = apiService.getDictionaryList()
        return if (response.status == Resource.Status.SUCCESS) {
            Resource.success(
                dictionaryEntityMapper.mapFromEntityList(response.data ?: emptyList())
            )
        } else {
            Resource.error(response.message ?: "")
        }
    }

    suspend fun getDictionary(id: String): Resource<List<Word>> {
        val response = apiService.getDictionary(id)
        return if (response.status == Resource.Status.SUCCESS) {
            Resource.success(
                wordEntityMapper.mapFromEntityList(response.data ?: emptyList())
            )
        } else {
            Resource.error(response.message ?: "")
        }
    }
}
