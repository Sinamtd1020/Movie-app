package com.sinamtd.movieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sinamtd.movieapp.data.local.dao.FavoriteMovieDao
import com.sinamtd.movieapp.data.local.entity.FavoriteMovieEntity

@Database(entities = [FavoriteMovieEntity::class],
    version = 1,
    exportSchema = false)
abstract class MovieDatabase : RoomDatabase(){

abstract fun favoriteMovieDao(): FavoriteMovieDao
}