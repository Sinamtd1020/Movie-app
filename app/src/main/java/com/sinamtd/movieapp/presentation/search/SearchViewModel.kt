package com.sinamtd.movieapp.presentation.search



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinamtd.movieapp.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<SearchUiState>(
            SearchUiState.Idle
        )

    val uiState: StateFlow<SearchUiState> =
        _uiState.asStateFlow()


    private val _query =
        MutableStateFlow("")

    val query =
        _query.asStateFlow()


    init {
        observeSearch()
    }


    fun onQueryChange(value: String) {
        _query.value = value
    }


    private fun observeSearch() {

        viewModelScope.launch {

            _query
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->

                    if (query.isBlank()) {

                        _uiState.value =
                            SearchUiState.Idle

                    } else {

                        searchMovies(query)
                    }
                }
        }
    }


    fun searchMovies(query: String) {

        if (query.isBlank()) {

            _uiState.value =
                SearchUiState.Idle

            return
        }


        viewModelScope.launch {

            _uiState.value =
                SearchUiState.Loading

            try {

                val result =
                    searchMoviesUseCase(query)


                if (result.isSuccess) {

                    val movies =
                        result.getOrNull().orEmpty()

                    _uiState.value =
                        SearchUiState.Success(
                            movies = movies
                        )

                } else {

                    _uiState.value =
                        SearchUiState.Error(
                            message =
                                result.exceptionOrNull()
                                    ?.message
                                    ?: "Something went wrong"
                        )
                }

            } catch (e: Exception) {

                _uiState.value =
                    SearchUiState.Error(
                        message =
                            e.message
                                ?: "Something went wrong"
                    )
            }
        }
    }


    fun retry(query: String) {

        searchMovies(query)
    }
}