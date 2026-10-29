package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.MovieVideo
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject


class GetMovieTrailerUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movieId: Int
    ): MovieVideo? {

        return repository.getMovieTrailer(movieId)
    }
}