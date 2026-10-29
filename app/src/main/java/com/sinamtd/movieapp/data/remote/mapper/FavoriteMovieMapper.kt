package com.sinamtd.movieapp.data.remote.mapper

import com.sinamtd.movieapp.data.local.entity.FavoriteMovieEntity
import com.sinamtd.movieapp.domain.model.MovieDetail

fun MovieDetail.toFavoriteEntity(): FavoriteMovieEntity{
    return FavoriteMovieEntity(
        id = id,
        title = title,
        posterPath = poster_path,
        backdropPath = backdrop_path,
        voteAverage = vote_average,
        releaseDate = release_date,
        overview = overview,
        voteCount = vote_count,
        runtime = runtime
    )
}

fun FavoriteMovieEntity.toDomain(): MovieDetail{
    return MovieDetail(
        id = id,
        title = title,
        poster_path = posterPath,
        backdrop_path = backdropPath,
        vote_average = voteAverage,
        release_date = releaseDate,
        overview = overview,
        vote_count = voteCount,
        runtime = runtime,
        genres = emptyList()

    )
}