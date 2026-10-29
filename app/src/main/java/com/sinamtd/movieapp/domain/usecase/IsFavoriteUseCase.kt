package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository){
     operator fun invoke(movieId: Int): Flow<Boolean>{
         return repository.isFavorite(movieId)
    }
}