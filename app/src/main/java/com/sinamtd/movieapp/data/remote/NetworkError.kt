package com.sinamtd.movieapp.data.remote

sealed class NetworkError (message: String): Exception(message){
    data object Unauthorized: NetworkError("Unauthorized request")

    data object NotFound: NetworkError("Movie not found")

    data object ServerError: NetworkError("Server error " +
            "Please try again later")

    data object NoInternet: NetworkError("No internet connection")

    data object Unknown: NetworkError("Something went wrong")

}