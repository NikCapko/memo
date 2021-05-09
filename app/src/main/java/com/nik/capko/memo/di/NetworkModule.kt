package com.nik.capko.memo.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nik.capko.memo.network.ApiService
import com.nik.capko.memo.network.ApiServiceImpl
import com.nik.capko.memo.network.RestConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(RestConfig.BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiServiceHelper(api: ApiService): ApiServiceImpl = ApiServiceImpl(api)
}
