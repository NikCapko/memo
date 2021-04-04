package com.nik.capko.memo.repository

import com.nik.capko.memo.db.AppDatabase
import com.nik.capko.memo.db.Word
import javax.inject.Inject

class WordRepository @Inject constructor(appDatabase: AppDatabase) {

    private val wordsDao = appDatabase.wordDao()

    fun saveWord(word: Word) {
        wordsDao.insert(word)
    }

    fun getAllWords(): List<Word> {
        return wordsDao.getAllWords()
    }
}
