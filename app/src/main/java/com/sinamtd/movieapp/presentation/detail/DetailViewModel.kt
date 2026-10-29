package com.sinamtd.movieapp.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.domain.usecase.AddFavoriteUseCase
import com.sinamtd.movieapp.domain.usecase.GetMovieCastUseCase
import com.sinamtd.movieapp.domain.usecase.GetMovieCrewUseCase
import com.sinamtd.movieapp.domain.usecase.GetMovieDetailsUseCase
import com.sinamtd.movieapp.domain.usecase.GetMovieTrailerUseCase
import com.sinamtd.movieapp.domain.usecase.GetSimilarMoviesUseCase
import com.sinamtd.movieapp.domain.usecase.IsFavoriteUseCase
import com.sinamtd.movieapp.domain.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getMovieCrewUseCase: GetMovieCrewUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase
): ViewModel(){
    private val _uiState= MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private val _isFavorite=MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _trailerState=MutableStateFlow(TrailerUiState())
    val trailerState = _trailerState.asStateFlow()

    private val _creditState=MutableStateFlow(CreditUiState())
    val creditState=_creditState.asStateFlow()

    private val _similarMoviesState=MutableStateFlow(SimilarMovieUiState())
    val similarMoviesState=_similarMoviesState.asStateFlow()
    private var loadedMovieId: Int?=null

    private var favoriteJob: Job?=null

    fun getTrailer(movieId: Int) {

        viewModelScope.launch {

            _trailerState.value =
                TrailerUiState(
                    isLoading = true
                )

            try {

                val trailer =
                    getMovieTrailerUseCase(movieId)

                _trailerState.value =
                    TrailerUiState(
                        isLoading = false,
                        trailer = trailer
                    )

            } catch (e: Exception) {
                Log.e("DETAIL_TRAILER","Trailer error",e)

                _trailerState.value =
                    TrailerUiState(
                        isLoading = false,
                        error = e.message
                            ?: "Unable to load trailer"
                    )
            }
        }
    }

    fun getCredits(movieId: Int) {

        viewModelScope.launch {

            _creditState.value =
                CreditUiState(
                    isLoading = true
                )

            try {

                val cast =
                    getMovieCastUseCase(movieId)

                val crew =
                    getMovieCrewUseCase(movieId)

                _creditState.value =
                    CreditUiState(
                        isLoading = false,
                        cast = cast,
                        crew = crew
                    )

            } catch (e: Exception) {

                _creditState.value =
                    CreditUiState(
                        isLoading = false,
                        error = e.message
                            ?: "Unable to load credits"
                    )
            }
        }
    }

    fun getSimilarMovies(movieId: Int) {
        if (loadedMovieId==movieId){
            return
        }
        loadedMovieId=movieId

        viewModelScope.launch {

            _similarMoviesState.value =
                SimilarMovieUiState(
                    isLoading = true
                )

            try {

                val movies =
                    getSimilarMoviesUseCase(movieId)

                _similarMoviesState.value =
                    SimilarMovieUiState(
                        isLoading = false,
                        movies = movies
                            .distinctBy { it.id }
                    )

            } catch (e: Exception) {

                _similarMoviesState.value =
                    SimilarMovieUiState(
                        isLoading = false,
                        error = e.message
                            ?: "Unable to load similar movies"
                    )
            }
        }
    }

    fun observeFavorite(movieId: Int){
        favoriteJob?.cancel()
        favoriteJob=viewModelScope.launch {
            isFavoriteUseCase(movieId)
                .collect {
                    _isFavorite.value=it
                }
        }
    }

    fun toggleFavorite(movie: MovieDetail){

        viewModelScope.launch {
            if (_isFavorite.value){
                removeFavoriteUseCase(movie.id)
        }
            else{
                addFavoriteUseCase(movie)
            }
    }}

    fun getMovieDetails(movieId: Int) {

        viewModelScope.launch {

            _uiState.value =
                MovieDetailUiState.Loading

            try {

                val movie =
                    getMovieDetailsUseCase(
                        movieId = movieId
                    )

                _uiState.value =
                    MovieDetailUiState.Success(
                        movie = movie
                    )

                observeFavorite(movieId)

                // اطلاعات تکمیلی
                getTrailer(movieId)

                getCredits(movieId)

                getSimilarMovies(movieId)

            } catch (e: Exception) {
                loadedMovieId=null

                _uiState.value =
                    MovieDetailUiState.Error(
                        message =
                            e.message
                                ?: "Something went wrong"
                    )
            }
        }
    }

    fun retry(movieId: Int){
        loadedMovieId=null
        getMovieDetails(movieId)
    }

    override fun onCleared() {
        favoriteJob?.cancel()
        super.onCleared()
    }
}