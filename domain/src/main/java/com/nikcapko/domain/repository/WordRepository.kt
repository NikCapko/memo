package com.nikcapko.domain.repository

import com.nikcapko.domain.model.WordModel

interface WordRepository {
    suspend fun saveWord(word: WordModel)
    suspend fun deleteWord(id: String)
    suspend fun getWordsFromDB(): List<WordModel>
    suspend fun getWordsForGameByLimit(limit: Int): List<WordModel>
    suspend fun clearDatabase()
}
