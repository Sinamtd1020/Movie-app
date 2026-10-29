package com.sinamtd.movieapp.presentation.home

import com.sinamtd.movieapp.domain.model.Movie

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(val movies: List<Movie>,val isLoadingMore: Boolean=false): HomeUiState

    data class Error(val message: String): HomeUiState
}