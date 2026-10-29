package com.sinamtd.movieapp.data.remote



import retrofit2.HttpException
import java.io.IOException

fun Throwable.toNetworkError():
        NetworkError{
    return when(this){
        is IOException -> NetworkError.NoInternet

        is HttpException -> {
            when(code()){
                401-> NetworkError.Unauthorized
                404 -> NetworkError.NotFound
                in 500..599 -> NetworkError.ServerError
                else -> NetworkError.Unknown
            }
        }

        else -> NetworkError.Unknown
    }
}