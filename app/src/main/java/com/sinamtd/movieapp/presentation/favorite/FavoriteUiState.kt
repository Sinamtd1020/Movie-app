package com.sinamtd.movieapp.presentation.favorite

import com.sinamtd.movieapp.domain.model.MovieDetail

sealed interface FavoriteUiState {
    data object Loading: FavoriteUiState

    data class Success(val movies: List<MovieDetail>): FavoriteUiState

    data class Error(val message: String): FavoriteUiState
}