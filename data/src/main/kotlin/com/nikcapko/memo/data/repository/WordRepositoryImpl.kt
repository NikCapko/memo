package com.nikcapko.memo.data.repository

import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.repository.WordRepository
import com.nikcapko.memo.data.db.AppDatabase
import com.nikcapko.memo.data.db.converter.WordDBEntityListConverter
import com.nikcapko.memo.data.db.converter.WordModelToEntityConverter
import javax.inject.Inject

internal class WordRepositoryImpl @Inject constructor(
    appDatabase: AppDatabase,
    private val wordDBEntityListConverter: WordDBEntityListConverter,
    private val wordModelToEntityConverter: WordModelToEntityConverter,
) : WordRepository {

    private val wordsDao = appDatabase.wordDao()

    override suspend fun saveWord(word: WordModel) {
        wordsDao.insert(wordModelToEntityConverter.convert(word))
    }

    override suspend fun deleteWord(id: String) {
        wordsDao.deleteWordById(id)
    }

    override suspend fun getWordsFromDB(): List<WordModel> {
        return wordDBEntityListConverter.convert(wordsDao.getAllWords())
    }

    override suspend fun getWordsForGameByLimit(limit: Int): List<WordModel> {
        return wordDBEntityListConverter.convert(wordsDao.getWordsForGameByLimit(limit))
    }

    override suspend fun clearDatabase() {
        wordsDao.removeAll()
    }
}
