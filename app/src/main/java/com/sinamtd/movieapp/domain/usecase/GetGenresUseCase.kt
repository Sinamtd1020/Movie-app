package com.sinamtd.movieapp.domain.usecase

import com.sinamtd.movieapp.domain.model.Genre
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(): Result<List<Genre>> {

        return repository.getGenres()
    }
}