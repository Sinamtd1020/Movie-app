package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val repository: MovieRepository){
    suspend operator fun invoke(movieId: Int): MovieDetail{
        return repository.getMovieDetails(movieId = movieId)
    }
}