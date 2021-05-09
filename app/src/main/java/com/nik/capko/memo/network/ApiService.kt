package com.nik.capko.memo.network

import com.nik.capko.memo.data.Word
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(RestConfig.GET_WORD_LIST)
    suspend fun getWordList(): Response<List<Word>>
}
