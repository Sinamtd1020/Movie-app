package com.sinamtd.movieapp.data.remote.api

import com.sinamtd.movieapp.data.remote.dto.GenreResponseDto
import com.sinamtd.movieapp.data.remote.dto.MovieCreditsDto
import com.sinamtd.movieapp.data.remote.dto.MovieDetailsDto
import com.sinamtd.movieapp.data.remote.dto.MovieResponseDto
import com.sinamtd.movieapp.data.remote.dto.MovieVideosResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("language") language: String="en-US",
        @Query("page") page: Int=1
    ): MovieResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDetailsDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int=1
    ): MovieResponseDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos
                (@Path("movie_id") movieId: Int,
                 @Query("language") language: String="en_US"
    ): MovieVideosResponseDto

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovie
                (@Path("movie_id") movieId: Int,
                 @Query("language") language: String="en_US",
                 @Query("page") page: Int=1
    ):MovieResponseDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits
                (@Path("movie_id") movieId: Int,
                 @Query("language") language: String="en_US"
    ): MovieCreditsDto

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String="en_US"
    ) : GenreResponseDto

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genreId: Int,
        @Query("language") language: String="en_US",
        @Query("page") page: Int=1
    ): MovieResponseDto

}