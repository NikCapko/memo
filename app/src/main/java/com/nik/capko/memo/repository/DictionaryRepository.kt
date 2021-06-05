package com.nik.capko.memo.repository

import com.nik.capko.memo.base.network.Resource
import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.network.ApiServiceImpl
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    api: ApiServiceImpl
) {
    private val apiService = api

    suspend fun getDictionaryList(): Resource<List<Dictionary>> {
        return apiService.getDictionaryList()
    }

    suspend fun getDictionary(id: String): Resource<List<Word>> {
        return apiService.getDictionary(id)
    }
}
