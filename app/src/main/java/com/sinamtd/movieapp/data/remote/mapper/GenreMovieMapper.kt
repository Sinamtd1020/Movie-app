package com.sinamtd.movieapp.data.remote.mapper

import com.sinamtd.movieapp.data.remote.dto.GenreDto
import com.sinamtd.movieapp.data.remote.dto.GenreResponseDto
import com.sinamtd.movieapp.domain.model.Genre

fun GenreDto.toDomain(): Genre{
    return Genre(
        id = id,
        name = name
    )

}
fun GenreResponseDto.toDomain(): List<Genre>{
    return genres.map { it.toDomain() }
}