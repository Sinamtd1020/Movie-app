package com.sinamtd.movieapp.presentation.favorite


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinamtd.movieapp.domain.usecase.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteUseCase: GetFavoriteUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<FavoriteUiState>(
            FavoriteUiState.Loading
        )

    val uiState =
        _uiState.asStateFlow()


    init {
        getFavorites()
    }


    private fun getFavorites() {

        viewModelScope.launch {

            try {

                getFavoriteUseCase()
                    .collect { movies ->

                        _uiState.value =
                            FavoriteUiState.Success(
                                movies = movies
                            )
                    }

            } catch (e: Exception) {

                _uiState.value =
                    FavoriteUiState.Error(
                        message =
                            e.message
                                ?: "Something went wrong"
                    )
            }
        }
    }


    fun retry() {
        getFavorites()
    }
}
