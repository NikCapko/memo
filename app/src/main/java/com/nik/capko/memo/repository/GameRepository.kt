package com.nik.capko.memo.repository

import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.AppDatabase
import com.nik.capko.memo.db.data.FormDBEntity
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.db.mapper.WordFormDBMapper
import javax.inject.Inject

class GameRepository @Inject constructor(
    appDatabase: AppDatabase,
    private var wordFormMapper: WordFormDBMapper,
) {

    private val wordsDao = appDatabase.wordDao()
    private val formsDao = appDatabase.formDao()

    suspend fun saveWord(word: WordDBEntity) {
        wordsDao.insert(word)
    }

    suspend fun saveForm(form: FormDBEntity) {
        formsDao.insert(form)
    }

    suspend fun getWords(): List<Word> {
        return wordFormMapper.mapFromEntityList(wordsDao.getAllWords())
    }

    suspend fun getWordsForGame(): List<Word> {
        return wordFormMapper.mapFromEntityList(wordsDao.getWordsForGame())
    }

    suspend fun getWordsForGameByLimit(limit: Int): List<Word> {
        return wordFormMapper.mapFromEntityList(wordsDao.getWordsForGameByLimit(limit))
    }
}
