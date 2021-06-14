package com.nik.capko.memo.repository

import com.nik.capko.memo.base.network.Resource
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.AppDatabase
import com.nik.capko.memo.db.data.FormDBEntity
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.db.mapper.WordFormDBMapper
import com.nik.capko.memo.network.ApiServiceImpl
import javax.inject.Inject

class WordRepository @Inject constructor(
    appDatabase: AppDatabase,
    var apiService: ApiServiceImpl,
    var wordFormMapper: WordFormDBMapper,
) {

    private val wordsDao = appDatabase.wordDao()
    private val formsDao = appDatabase.formDao()

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
        return wordFormMapper.mapFromEntityList(wordsDao.getAllWords())
    }

    suspend fun getWordsFromServer(): Resource<List<Word>> {
        return apiService.getWordList()
    }

    suspend fun clearDatabase() {
        formsDao.removeAll()
        wordsDao.removeAll()
    }
}
