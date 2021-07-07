package com.nik.capko.memo.repository

import com.nik.capko.memo.R
import com.nik.capko.memo.base.network.Resource
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.AppDatabase
import com.nik.capko.memo.db.data.FormDBEntity
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.db.mapper.WordFormDBEntityMapper
import com.nik.capko.memo.network.ApiServiceImpl
import com.nik.capko.memo.network.mapper.WordEntityMapper
import com.nik.capko.memo.utils.resources.FieldConverter
import javax.inject.Inject

class WordRepository @Inject constructor(
    appDatabase: AppDatabase,
    private val apiService: ApiServiceImpl,
    private val wordFormEntityMapper: WordFormDBEntityMapper,
    private val wordEntityMapper: WordEntityMapper,
    private val fieldConverter: FieldConverter,
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
        return wordFormEntityMapper.mapFromEntityList(wordsDao.getAllWords())
    }

    suspend fun getWordsFromServer(): Resource<List<Word>> {
        val response = apiService.getWordList()
        return if (response.status == Resource.Status.SUCCESS) {
            Resource.success(
                wordEntityMapper.mapFromEntityList(response.data ?: emptyList())
            )
        } else {
            Resource.error(response.message ?: fieldConverter.getString(R.string.error_default_message))
        }
    }

    suspend fun clearDatabase() {
        formsDao.removeAll()
        wordsDao.removeAll()
    }
}
