package com.sinamtd.movieapp.di


import android.content.Context
import androidx.room.Room
import com.sinamtd.movieapp.data.local.dao.FavoriteMovieDao
import com.sinamtd.movieapp.data.local.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
@Provides
@Singleton
fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase{
    return Room.databaseBuilder(context, MovieDatabase::class.java,"favorite_movie").build()
}
@Provides
@Singleton
fun provideFavoriteMovieDao(database: MovieDatabase): FavoriteMovieDao{
    return database.favoriteMovieDao()
}
}