package com.nik.capko.memo.di

import android.content.Context
import com.nik.capko.memo.utils.resources.FieldConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ResourcesModule {
    @Provides
    fun provideFieldConverter(@ApplicationContext context: Context): FieldConverter {
        return FieldConverter(context)
    }
}
