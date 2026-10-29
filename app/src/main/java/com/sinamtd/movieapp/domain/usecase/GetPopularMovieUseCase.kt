package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMovieUseCase @Inject constructor(private val repository: MovieRepository){
suspend operator fun invoke(page:Int=1): List<Movie>{
    return repository.getPopularMovies(page = page)
}}