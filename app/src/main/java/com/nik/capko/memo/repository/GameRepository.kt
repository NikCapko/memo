package com.nik.capko.memo.repository

import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.AppDatabase
import com.nik.capko.memo.db.data.FormDBEntity
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.db.data.WordFormDBEntity.Companion.toWordModel
import javax.inject.Inject

class GameRepository @Inject constructor(
    appDatabase: AppDatabase
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
        return wordsDao.getAllWords().map { it.toWordModel() }
    }

    suspend fun getWordsForGame(): List<Word> {
        return wordsDao.getWordsForGame().map { it.toWordModel() }
    }

    suspend fun getWordsForGameByLimit(limit: Int): List<Word> {
        return wordsDao.getWordsForGameByLimit(limit).map { it.toWordModel() }
    }
}
