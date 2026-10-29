package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.CastMember
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieCastUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movieId: Int
    ): List<CastMember> {

        return repository.getMovieCast(movieId)
    }
}