package com.nikcapko.memo.data.db.base

import com.nikcapko.core.network.Resource
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
            return when (e) {
                is SocketTimeoutException -> error(message = "connection error!")
                is ConnectException -> error(message = "no internet access!")
                is UnknownHostException -> error(message = "no internet access!")
                else -> error(e.message ?: e.toString())
            }
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error(message)
    }
}
