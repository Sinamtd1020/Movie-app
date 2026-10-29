package com.sinamtd.movieapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.usecase.GetPopularMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMovieUseCase: GetPopularMovieUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<HomeUiState>(
            HomeUiState.Loading
        )

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()


    private var currentPage = 1

    private var isLoadingMore = false

    private var hasMorePages = true


    init {
        getPopularMovies()
    }


    fun getPopularMovies() {

        viewModelScope.launch {

            _uiState.value =
                HomeUiState.Loading

            try {

                currentPage = 1
                hasMorePages = true
                isLoadingMore=false

                val movies =
                    getPopularMovieUseCase(
                        page = currentPage
                    )

                hasMorePages =
                    movies.isNotEmpty()

                _uiState.value =
                    HomeUiState.Success(
                        movies = movies.distinctBy { it.id },
                        isLoadingMore = false
                    )

            } catch (e: Exception) {

                _uiState.value =
                    HomeUiState.Error(
                        message =
                            e.message
                                ?: "Something is wrong"
                    )
            }
        }
    }


    fun loadNextPage() {

        // جلوگیری از درخواست همزمان
        if (isLoadingMore) return

        // اگر صفحه بعدی نداریم
        if (!hasMorePages) return


        val currentState =
            _uiState.value

        // فقط در حالت Success می‌توانیم Pagination کنیم
        if (currentState !is HomeUiState.Success) {
            return
        }


         isLoadingMore = true


        _uiState.value =
            currentState.copy(
                isLoadingMore = true
            )


        viewModelScope.launch {

            try {

                val nextPage =
                    currentPage + 1

                val newMovies =
                    getPopularMovieUseCase(
                        page = nextPage
                    )


                val currentMovies =
                    (_uiState.value as? HomeUiState.Success)
                        ?.movies
                        .orEmpty()


                val updatedMovies =
                    (currentMovies + newMovies)
                        .distinctBy {
                            it.id
                        }


                // اگر صفحه جدید خالی بود
                hasMorePages =
                    newMovies.isNotEmpty()


                if (newMovies.isNotEmpty()) {
                    currentPage = nextPage
                }


                _uiState.value =
                    HomeUiState.Success(
                        movies = updatedMovies,
                        isLoadingMore = false
                    )


                isLoadingMore = false


            } catch (e: Exception) {

                isLoadingMore = false

                _uiState.value =
                    currentState.copy(
                        isLoadingMore = false
                    )
            }
        }
    }


    fun retry() {
        getPopularMovies()
    }
}