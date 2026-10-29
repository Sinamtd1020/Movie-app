package com.sinamtd.movieapp.presentation.detail

import com.sinamtd.movieapp.domain.model.MovieDetail

sealed interface MovieDetailUiState {
    data object Loading: MovieDetailUiState
    data class Success(val movie: MovieDetail): MovieDetailUiState
    data class Error(val message: String): MovieDetailUiState
}