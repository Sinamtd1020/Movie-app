package com.sinamtd.movieapp.data.remote.dto
data class MovieVideosResponseDto(
    val id: Int,
    val results: List<MovieVideoDto>?
)
data class MovieVideoDto(
    val id: String,
    val key: String?,
    val name: String?,
    val site: String?,
    val type: String?,
    val official: Boolean?
)