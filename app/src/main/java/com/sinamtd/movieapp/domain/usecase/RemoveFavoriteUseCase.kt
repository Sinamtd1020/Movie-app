package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository){
    suspend operator fun invoke(movieId: Int){
        repository.removeFavorite(movieId)
    }
}