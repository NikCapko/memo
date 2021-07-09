package com.nik.capko.memo.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nik.capko.memo.network.ApiService
import com.nik.capko.memo.network.ApiServiceImpl
import com.nik.capko.memo.network.RestConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val READ_TIMEOUT = 30
    private const val WRITE_TIMEOUT = 30
    private const val CONNECTION_TIMEOUT = 10
    private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okhttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(RestConfig.BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okhttpClient)
        .build()

    @Provides
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            cache(cache)
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
        }.build()
    }

    @Provides
    @Singleton
    internal fun provideCache(@ApplicationContext context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiServiceHelper(api: ApiService): ApiServiceImpl = ApiServiceImpl(api)
}
