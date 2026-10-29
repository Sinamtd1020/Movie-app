package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.CrewMember
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieCrewUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        movieId: Int
    ): List<CrewMember> {

        return repository.getMovieCrew(movieId)
    }
}