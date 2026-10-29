package com.sinamtd.movieapp.presentation.detail

import com.sinamtd.movieapp.domain.model.MovieVideo

data class TrailerUiState(
    val isLoading: Boolean=false,
    val trailer: MovieVideo?=null,
    val error: String?=null
)
