package com.nikcapko.memo.data.di

import com.nikcapko.domain.repository.WordRepository
import com.nikcapko.memo.data.db.AppDatabase
import com.nikcapko.memo.data.db.mapper.WordDBEntityMapper
import com.nikcapko.memo.data.repository.WordRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {
    @Suppress("LongParameterList")
    @Provides
    fun provideWordRepository(
        appDatabase: AppDatabase,
        wordDBEntityMapper: WordDBEntityMapper,
    ): WordRepository {
        return WordRepositoryImpl(
            appDatabase = appDatabase,
            wordDBEntityMapper = wordDBEntityMapper,
        )
    }
}
