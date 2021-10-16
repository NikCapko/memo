package com.nikcapko.domain.repository

import com.nikcapko.core.network.Resource
import com.nikcapko.domain.model.FormModel
import com.nikcapko.domain.model.WordModel

interface IWordRepository {
    suspend fun saveWord(word: WordModel)
    suspend fun saveForm(form: FormModel)
    suspend fun deleteWord(wordModel: WordModel)
    suspend fun getWordsFromDB(): List<WordModel>
    suspend fun getWordsFromServer(): Resource<List<WordModel>>
    suspend fun getWordsForGame(): List<WordModel>
    suspend fun getWordsForGameByLimit(limit: Int): List<WordModel>
    suspend fun clearDatabase()
}
