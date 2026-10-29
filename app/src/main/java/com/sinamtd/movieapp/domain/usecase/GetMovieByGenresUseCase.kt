package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        genreId: Int,
        page: Int = 1
    ): Result< List<Movie>> {

        return repository.getMovieByGenres(
            genreId = genreId,
            page = page
        )
    }
}