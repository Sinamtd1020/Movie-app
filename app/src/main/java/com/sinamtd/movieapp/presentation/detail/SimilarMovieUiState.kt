package com.sinamtd.movieapp.presentation.detail

import com.sinamtd.movieapp.domain.model.Movie

data class SimilarMovieUiState(
    val isLoading: Boolean=false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)
