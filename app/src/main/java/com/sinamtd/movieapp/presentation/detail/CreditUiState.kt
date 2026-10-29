package com.sinamtd.movieapp.presentation.detail

import com.sinamtd.movieapp.domain.model.CastMember
import com.sinamtd.movieapp.domain.model.CrewMember

data class CreditUiState(
    val isLoading: Boolean=false,
    val cast: List<CastMember> = emptyList(),
    val crew: List<CrewMember> = emptyList(),
    val error: String?=null
)
