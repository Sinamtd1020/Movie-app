package com.sinamtd.movieapp.presentation.search

import com.sinamtd.movieapp.domain.model.Movie

sealed interface SearchUiState {

    data object Idle: SearchUiState

    data object Loading: SearchUiState

    data class Success(val movies: List<Movie>): SearchUiState

    data class Error(val message: String): SearchUiState
}