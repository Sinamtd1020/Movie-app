package com.sinamtd.movieapp.di

import androidx.compose.runtime.internal.DecoyImplementation
import com.sinamtd.movieapp.data.repository.FavoriteRepositoryImpl
import com.sinamtd.movieapp.data.repository.MovieRepositoryImpl
import com.sinamtd.movieapp.domain.repository.FavoriteRepository
import com.sinamtd.movieapp.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
    abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository (repositoryImpl: MovieRepositoryImpl): MovieRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(implementation: FavoriteRepositoryImpl): FavoriteRepository
}