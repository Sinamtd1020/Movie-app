package com.sinamtd.movieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sinamtd.movieapp.data.local.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao{
    @Insert
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id=:movieId")
    suspend fun deleteFavorite(movieId: Int)

    @Query("SELECT * FROM favorite_movies")
    fun getFavorites(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_movies WHERE id=:movieId )")
    fun isFavorite(movieId: Int): Flow<Boolean>
}