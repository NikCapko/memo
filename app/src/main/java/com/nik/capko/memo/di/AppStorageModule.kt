package com.nik.capko.memo.di

import android.content.Context
import com.nik.capko.memo.utils.AppStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppStorageModule {
    @Provides
    fun provideAppStorage(@ApplicationContext context: Context): AppStorage {
        return AppStorage(context)
    }
}
