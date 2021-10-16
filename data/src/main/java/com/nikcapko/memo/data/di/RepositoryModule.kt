package com.nikcapko.memo.data.di

import com.nikcapko.domain.repository.IDictionaryRepository
import com.nikcapko.domain.repository.IWordRepository
import com.nikcapko.memo.data.db.AppDatabase
import com.nikcapko.memo.data.db.mapper.FormDBEntityMapper
import com.nikcapko.memo.data.db.mapper.WordDBEntityMapper
import com.nikcapko.memo.data.db.mapper.WordFormDBEntityMapper
import com.nikcapko.memo.data.network.ApiServiceImpl
import com.nikcapko.memo.data.network.mapper.DictionaryEntityMapper
import com.nikcapko.memo.data.network.mapper.WordEntityMapper
import com.nikcapko.memo.data.repository.DictionaryRepository
import com.nikcapko.memo.data.repository.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideDictionaryRepository(
        apiService: ApiServiceImpl,
        dictionaryEntityMapper: DictionaryEntityMapper,
        wordEntityMapper: WordEntityMapper,
    ): IDictionaryRepository {
        return DictionaryRepository(apiService, dictionaryEntityMapper, wordEntityMapper)
    }

    @Suppress("LongParameterList")
    @Provides
    fun provideWordRepository(
        appDatabase: AppDatabase,
        apiService: ApiServiceImpl,
        wordFormDBEntityMapper: WordFormDBEntityMapper,
        wordDBEntityMapper: WordDBEntityMapper,
        wordEntityMapper: WordEntityMapper,
        formDBEntityMapper: FormDBEntityMapper,
    ): IWordRepository {
        return WordRepository(
            appDatabase,
            apiService,
            wordFormDBEntityMapper,
            wordDBEntityMapper,
            wordEntityMapper,
            formDBEntityMapper
        )
    }
}
