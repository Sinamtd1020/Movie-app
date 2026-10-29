package com.sinamtd.movieapp.domain.model

data class CrewMember(
    val id: Int,
    val name: String,
    val job: String,
    val department: String,
    val profilePath: String?
)