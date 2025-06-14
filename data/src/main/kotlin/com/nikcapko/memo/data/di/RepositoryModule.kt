package com.nikcapko.memo.data.di

import com.nikcapko.memo.domain.repository.WordRepository
import com.nikcapko.memo.data.database.AppDatabase
import com.nikcapko.memo.data.database.converter.WordDBEntityListConverter
import com.nikcapko.memo.data.database.converter.WordModelToEntityConverter
import com.nikcapko.memo.data.repository.WordRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {
    @Provides
    fun provideWordRepository(
        appDatabase: AppDatabase,
        wordDBEntityListConverter: WordDBEntityListConverter,
        wordModelToEntityConverter: WordModelToEntityConverter,
    ): WordRepository {
        return WordRepositoryImpl(
            appDatabase = appDatabase,
            wordDBEntityListConverter = wordDBEntityListConverter,
            wordModelToEntityConverter = wordModelToEntityConverter,
        )
    }
}
