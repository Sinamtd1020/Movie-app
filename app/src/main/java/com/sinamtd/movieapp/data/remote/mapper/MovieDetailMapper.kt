package com.sinamtd.movieapp.data.remote.mapper

import com.sinamtd.movieapp.data.remote.dto.MovieDetailsDto
import com.sinamtd.movieapp.domain.model.Genre
import com.sinamtd.movieapp.domain.model.MovieDetail

fun MovieDetailsDto.toDomain(): MovieDetail{
    return MovieDetail(
        id = id,
        title = title,
        overview = overview,
        poster_path = poster_path,
        backdrop_path = backdrop_path,
        release_date = release_date,
        vote_average = vote_average,
        vote_count = vote_count,
        runtime = runtime,
        genres = genres?.map { Genre(id = it.id, name = it.name) }?:emptyList()
    )
}