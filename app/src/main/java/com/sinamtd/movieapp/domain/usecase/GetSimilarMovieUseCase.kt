package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movieId: Int
    ): List<Movie> {

        return repository.getSimilarMovies(movieId)
    }
}