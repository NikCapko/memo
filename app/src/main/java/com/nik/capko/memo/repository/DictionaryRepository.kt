package com.nik.capko.memo.repository

import com.nik.capko.memo.base.network.Resource
import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.AppDatabase
import com.nik.capko.memo.db.data.FormDBEntity
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.network.ApiServiceImpl
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    appDatabase: AppDatabase,
    api: ApiServiceImpl
) {
    private val wordsDao = appDatabase.wordDao()
    private val formsDao = appDatabase.formDao()

    private val apiService = api

    suspend fun saveWord(word: WordDBEntity) {
        wordsDao.insert(word)
    }

    suspend fun saveForm(form: FormDBEntity) {
        formsDao.insert(form)
    }

    suspend fun getDictionaryList(): Resource<List<Dictionary>> {
        return apiService.getDictionaryList()
    }

    suspend fun getDictionary(id: String): Resource<List<Word>> {
        return apiService.getDictionary(id)
    }
}
