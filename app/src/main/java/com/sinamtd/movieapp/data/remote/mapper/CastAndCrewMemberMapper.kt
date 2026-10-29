package com.sinamtd.movieapp.data.remote.mapper

import com.sinamtd.movieapp.data.remote.dto.CastMemberDto
import com.sinamtd.movieapp.data.remote.dto.CrewMemberDto
import com.sinamtd.movieapp.domain.model.CastMember
import com.sinamtd.movieapp.domain.model.CrewMember

fun CastMemberDto.toDomain(): CastMember? {

    val memberName = name ?: return null

    return CastMember(
        id = id,
        name = memberName,
        character = character ?: "",
        profilePath = profile_path
    )
}

fun CrewMemberDto.toDomain(): CrewMember? {

    val memberName = name ?: return null

    return CrewMember(
        id = id,
        name = memberName,
        job = job ?: "",
        department = department ?: "",
        profilePath = profile_path
    )
}