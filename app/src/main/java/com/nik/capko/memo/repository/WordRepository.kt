package com.nik.capko.memo.repository

import com.nik.capko.memo.base.network.Resource
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.AppDatabase
import com.nik.capko.memo.db.data.FormDBEntity
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.db.data.WordFormDBEntity.Companion.toWordModel
import com.nik.capko.memo.network.ApiServiceImpl
import javax.inject.Inject

class WordRepository @Inject constructor(
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

    suspend fun deleteWordById(id: Long) {
        wordsDao.deleteWordById(id)
    }

    suspend fun getWordsFromDB(): List<Word> {
        return wordsDao.getAllWords().map { it.toWordModel() }
    }

    suspend fun getWordsFromServer(): Resource<List<Word>> {
        return apiService.getWordList()
    }

    suspend fun clearDatabase() {
        formsDao.removeAll()
        wordsDao.removeAll()
    }
}
