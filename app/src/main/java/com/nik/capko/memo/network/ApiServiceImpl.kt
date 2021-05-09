package com.nik.capko.memo.network

import com.nik.capko.memo.network.base.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiServiceImpl @Inject constructor(
    private val apiService: ApiService
) : BaseDataSource() {
    suspend fun getWordList() = getResult { apiService.getWordList() }
}
