package com.nikcapko.memo.data.repository

import com.nikcapko.core.network.Resource
import com.nikcapko.domain.model.FormModel
import com.nikcapko.domain.model.WordModel
import com.nikcapko.domain.repository.IWordRepository
import com.nikcapko.memo.data.db.AppDatabase
import com.nikcapko.memo.data.db.mapper.FormDBEntityMapper
import com.nikcapko.memo.data.db.mapper.WordDBEntityMapper
import com.nikcapko.memo.data.db.mapper.WordFormDBEntityMapper
import com.nikcapko.memo.data.network.ApiServiceImpl
import com.nikcapko.memo.data.network.mapper.WordEntityMapper
import javax.inject.Inject

class WordRepository @Inject constructor(
    appDatabase: AppDatabase,
    private val apiService: ApiServiceImpl,
    private val wordFormDBEntityMapper: WordFormDBEntityMapper,
    private val wordDBEntityMapper: WordDBEntityMapper,
    private val wordEntityMapper: WordEntityMapper,
    private val formDBEntityMapper: FormDBEntityMapper,
) : IWordRepository {
    private val wordsDao = appDatabase.wordDao()
    private val formsDao = appDatabase.formDao()

    override suspend fun saveWord(word: WordModel) {
        wordsDao.insert(wordDBEntityMapper.mapToEntity(word))
    }

    override suspend fun saveForm(form: FormModel) {
        formsDao.insert(formDBEntityMapper.mapToEntity(form))
    }

    override suspend fun deleteWord(wordModel: WordModel) {
        wordsDao.delete(wordDBEntityMapper.mapToEntity(wordModel))
    }

    override suspend fun getWordsFromDB(): List<WordModel> {
        return wordFormDBEntityMapper.mapFromEntityList(wordsDao.getAllWords())
    }

    override suspend fun getWordsFromServer(): Resource<List<WordModel>> {
        val response = apiService.getWordList()
        return if (response.status == Resource.Status.SUCCESS) {
            Resource.success(
                wordEntityMapper.mapFromEntityList(response.data ?: emptyList())
            )
        } else {
            Resource.error(response.message ?: "")
        }
    }

    override suspend fun getWordsForGame(): List<WordModel> {
        return wordFormDBEntityMapper.mapFromEntityList(wordsDao.getWordsForGame())
    }

    override suspend fun getWordsForGameByLimit(limit: Int): List<WordModel> {
        return wordFormDBEntityMapper.mapFromEntityList(wordsDao.getWordsForGameByLimit(limit))
    }

    override suspend fun clearDatabase() {
        formsDao.removeAll()
        wordsDao.removeAll()
    }
}
