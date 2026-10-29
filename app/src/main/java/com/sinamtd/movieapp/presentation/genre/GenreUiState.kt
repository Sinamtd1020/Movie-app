package com.sinamtd.movieapp.presentation.genre

import com.sinamtd.movieapp.domain.model.Genre
import com.sinamtd.movieapp.domain.model.Movie

data class GenreUiState(
    val genres: List<Genre> = emptyList(),
    val selectedGenreId: Int?=null,
    val movies: List<Movie> = emptyList(),
    val isLoadingGenres: Boolean=false,
    val isLoadingMovies: Boolean=false,
    val isLoadingMore: Boolean=false,
    val error: String? =null
)