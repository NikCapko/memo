package com.nik.capko.memo.base.network

import retrofit2.Response

@Suppress("UnnecessaryAbstractClass")
abstract class BaseDataSource {

    @Suppress("ReturnCount", "TooGenericExceptionCaught")
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.success(body)
                }
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error(message)
    }
}
