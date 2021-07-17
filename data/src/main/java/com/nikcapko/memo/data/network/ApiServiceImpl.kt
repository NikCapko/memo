package com.nikcapko.memo.data.network

import com.nikcapko.memo.data.db.base.BaseRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiServiceImpl @Inject constructor(
    private val apiService: ApiService,
) : BaseRemoteDataSource() {

    suspend fun getWordList() = getResult { apiService.getWordList() }

    suspend fun getDictionaryList() = getResult { apiService.getDictionaryList() }

    suspend fun getDictionary(id: String) = getResult { apiService.getDictionary(id) }
}
