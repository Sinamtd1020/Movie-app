package com.sinamtd.movieapp.data.remote.mapper

import com.sinamtd.movieapp.data.remote.dto.MovieCreditsDto
import com.sinamtd.movieapp.domain.model.CastMember
import com.sinamtd.movieapp.domain.model.CrewMember

fun MovieCreditsDto.castToDomain(): List<CastMember> {

    return cast
        .sortedBy { it.order ?: Int.MAX_VALUE }
        .mapNotNull {
            it.toDomain()
        }
}
fun MovieCreditsDto.crewToDomain(): List<CrewMember> {

    return crew.mapNotNull {
        it.toDomain()
    }
}
