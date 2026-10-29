package com.sinamtd.movieapp.data.remote.dto

import com.sinamtd.movieapp.domain.model.Genre

data class GenreResponseDto(
    val genres: List<GenreDto>
)