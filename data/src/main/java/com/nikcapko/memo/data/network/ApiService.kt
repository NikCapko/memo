package com.nikcapko.memo.data.network

import com.nikcapko.memo.data.network.data.DictionaryEntity
import com.nikcapko.memo.data.network.data.WordEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(RestConfig.GET_WORD_LIST)
    suspend fun getWordList(): Response<List<WordEntity>>

    @GET(RestConfig.GET_DICTIONARY_LIST)
    suspend fun getDictionaryList(): Response<List<DictionaryEntity>>

    @GET(RestConfig.GET_DICTIONARY)
    suspend fun getDictionary(@Path("id") id: String): Response<List<WordEntity>>
}
