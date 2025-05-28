package com.nikcapko.memo.di

import com.nikcapko.memo.base.coroutines.DefaultDispatcherProvider
import com.nikcapko.memo.core.common.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {
    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}
