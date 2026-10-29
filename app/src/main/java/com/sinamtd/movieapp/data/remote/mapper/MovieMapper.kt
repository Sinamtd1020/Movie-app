package com.sinamtd.movieapp.data.remote.mapper

import com.sinamtd.movieapp.data.remote.dto.MovieDto
import com.sinamtd.movieapp.domain.model.Movie

fun MovieDto.toDomain() : Movie{
    return Movie(
        id = id,
        title=title,
        overview = overview,
        poster_path = poster_path,
        backdrop_path = backdrop_path,
        vote_average = vote_average,
        release_date = release_date
    )
}