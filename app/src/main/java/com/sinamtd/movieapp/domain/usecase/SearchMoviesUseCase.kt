package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class SearchMoviesUseCase  @Inject constructor(private val repository: MovieRepository){
    suspend operator fun invoke(query: String): Result<List<Movie>>{
        return repository.searchMovies(query)
    }
}