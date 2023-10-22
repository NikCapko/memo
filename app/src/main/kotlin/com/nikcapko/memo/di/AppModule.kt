package com.nikcapko.memo.di

import android.content.Context
import com.nikcapko.memo.base.coroutines.DefaultDispatcherProvider
import com.nikcapko.memo.base.coroutines.DispatcherProvider
import com.nikcapko.memo.utils.resources.FieldConverter
import com.nikcapko.memo.utils.resources.ResourceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {
    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager =
        FieldConverter(context)
}
