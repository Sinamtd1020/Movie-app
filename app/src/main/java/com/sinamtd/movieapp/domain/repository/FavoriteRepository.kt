package com.sinamtd.movieapp.domain.repository

import com.sinamtd.movieapp.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository{
    suspend fun addFavorite(movie: MovieDetail)

    suspend fun removeFavorite(movieId: Int)

    fun getFavorites(): Flow<List<MovieDetail>>

    fun isFavorite(movieId: Int): Flow<Boolean>
}