package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository){
    operator fun invoke(): Flow<List<MovieDetail>>{
       return repository.getFavorites()
    }
}