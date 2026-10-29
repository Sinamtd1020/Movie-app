package com.sinamtd.movieapp.domain.repository

import com.sinamtd.movieapp.domain.model.CastMember
import com.sinamtd.movieapp.domain.model.CrewMember
import com.sinamtd.movieapp.domain.model.Genre
import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.domain.model.MovieVideo

interface MovieRepository {
    suspend fun getPopularMovies(page: Int=1): List<Movie>

    suspend fun getMovieDetails(movieId: Int): MovieDetail

    suspend fun searchMovies(query: String): Result<List<Movie>>

    suspend fun getMovieTrailer(movieId: Int): MovieVideo?

    suspend fun getMovieCast(movieId: Int): List<CastMember>

    suspend fun getMovieCrew(movieId: Int): List<CrewMember>


    suspend fun getSimilarMovies(movieId: Int):  List<Movie>

    suspend fun getGenres(): Result <List<Genre>>

    suspend fun getMovieByGenres(genreId: Int,page: Int=1): Result <List<Movie>>


}