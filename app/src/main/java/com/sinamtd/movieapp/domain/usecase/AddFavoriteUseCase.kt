package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository){
    suspend operator fun invoke(movie: MovieDetail){
        repository.addFavorite(movie)
    }
}