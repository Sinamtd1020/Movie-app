package com.sinamtd.movieapp.data.remote.dto

data class MovieCreditsDto (
    val id: Int,
    val cast: List<CastMemberDto>,
    val crew: List<CrewMemberDto>
)

data class CastMemberDto(
    val id: Int,
    val name: String?,
    val character: String?,
    val profile_path: String?,
    val order: Int?=null
)

data class CrewMemberDto(
    val id: Int,
    val name: String?,
    val job: String?,
    val department: String?,
    val profile_path: String?
)