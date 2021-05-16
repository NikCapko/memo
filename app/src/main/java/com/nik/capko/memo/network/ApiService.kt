package com.nik.capko.memo.network

import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.data.Word
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(RestConfig.GET_WORD_LIST)
    suspend fun getWordList(): Response<List<Word>>

    @GET(RestConfig.GET_DICTIONARY_LIST)
    suspend fun getDictionaryList(): Response<List<Dictionary>>

    @GET(RestConfig.GET_DICTIONARY)
    suspend fun getDictionary(@Path("id") id: String): Response<List<Word>>
}
