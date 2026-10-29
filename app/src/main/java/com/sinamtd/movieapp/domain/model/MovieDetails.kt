package com.sinamtd.movieapp.domain.model



data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double,
    val vote_count: Int,
    val runtime: Int?,
    val genres: List<Genre>?
)
data class Genre(
    val id: Int,
    val name: String
)