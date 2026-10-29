package com.sinamtd.movieapp.presentation.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinamtd.movieapp.domain.usecase.GetGenresUseCase
import com.sinamtd.movieapp.domain.usecase.GetMoviesByGenreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(GenreUiState())

    val uiState =
        _uiState.asStateFlow()

    private var currentPage=1
    private var isLoadingMore=false
    private var hasMorePages=true


    init {
        loadGenres()
    }


    fun loadGenres() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoadingGenres = true,
                    error = null
                )
            }

            try {

                val result =
                    getGenresUseCase()
                if (result.isSuccess){
                    val genres=result.getOrNull()
                        .orEmpty()
                _uiState.update {
                    it.copy(
                        genres =genres,
                        isLoadingGenres = false
                    )
                }}
                else{
                    _uiState.update {
                        it.copy(isLoadingGenres = false, error = result.exceptionOrNull()?.message?:"Unable to load genres")
                    }
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoadingGenres = false,
                        error =
                            e.message
                                ?: "Unable to load genres"
                    )
                }
            }
        }
    }


    fun selectGenre(
        genreId: Int
    ) {

        if (
            _uiState.value.selectedGenreId == genreId
        ) {
            return
        }
        currentPage=1
        hasMorePages=true
        _uiState.update {
            it.copy(selectedGenreId = genreId, movies = emptyList(), isLoadingMovies = true, isLoadingMore = false, error = null)
        }
        loadMoviesByGenre(genreId = genreId, page = 1)

    }

    fun loadNextPage() {

        val genreId =
            _uiState.value.selectedGenreId
                ?: return

        if (
            isLoadingMore ||
            !hasMorePages
        ) {
            return
        }

        isLoadingMore = true

        _uiState.update {
            it.copy(
                isLoadingMore = true
            )
        }

        loadMoviesByGenre(
            genreId = genreId,
            page = currentPage + 1
        )
    }
    fun loadMoviesByGenre(genreId: Int,page: Int){
        viewModelScope.launch {
            try {
                val result=getMoviesByGenreUseCase(genreId = genreId,page=page)
                if (result.isSuccess){
                    val newMovies=result.getOrNull()
                        .orEmpty()
                    val currentMovies=_uiState.value.movies
                    val updateMovies=if (page==1){
                        newMovies
                    }else{(currentMovies+newMovies).distinctBy { it.id } }
                    hasMorePages=newMovies.isNotEmpty()
                    currentPage=page
                    _uiState.update { it.copy(isLoadingMovies = false, isLoadingMore = false, movies = updateMovies) }
                }
                else{
                    _uiState.update { it.copy(isLoadingMovies = false, error = result.exceptionOrNull()?.message?:"Unable to load movies") }
                }
            }
            catch (e: Exception){
                _uiState.update { it.copy(isLoadingMovies = false, isLoadingMore = false, error = e.message?:"SSomething went wrong") }
            }
        }

    }

    fun retry() {

        val genreId =
            _uiState.value.selectedGenreId

        if (genreId == null) {
            loadGenres()
        } else {

            currentPage = 1
            hasMorePages = true

            _uiState.update {
                it.copy(
                    movies = emptyList(),
                    isLoadingMovies = true,
                    isLoadingMore = false,
                    error = null
                )
            }

            loadMoviesByGenre(
                genreId = genreId,
                page = 1
            )
        }
    }
    fun clearGenre() {

        currentPage = 1
        hasMorePages = true
        isLoadingMore = false

        _uiState.update {

            it.copy(
                selectedGenreId = null,
                movies = emptyList(),
                isLoadingMovies = false,
                isLoadingMore = false,
                error = null
            )
        }
    }
}