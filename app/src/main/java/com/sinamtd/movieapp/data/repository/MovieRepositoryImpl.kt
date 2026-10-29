package com.sinamtd.movieapp.data.repository

import com.sinamtd.movieapp.data.remote.api.MovieApi
import com.sinamtd.movieapp.data.remote.mapper.castToDomain
import com.sinamtd.movieapp.data.remote.mapper.crewToDomain
import com.sinamtd.movieapp.data.remote.mapper.toDomain
import com.sinamtd.movieapp.data.remote.mapper.toTrailer
import com.sinamtd.movieapp.data.remote.toNetworkError
import com.sinamtd.movieapp.domain.model.CastMember
import com.sinamtd.movieapp.domain.model.CrewMember
import com.sinamtd.movieapp.domain.model.Genre
import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.domain.model.MovieVideo
import com.sinamtd.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieApi: MovieApi): MovieRepository{
    override suspend fun getPopularMovies(page: Int): List<Movie> {
       try {

        return movieApi.getPopularMovie(page = page)
            .results
            .map { it.toDomain() }}
       catch (e: Exception){
           throw e.toNetworkError()
       }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetail {
        try {

            return movieApi.getMovieDetail(movieId = movieId).toDomain()
        }
        catch (e: Exception){throw e.toNetworkError()}
    }

    override suspend fun searchMovies(query: String): Result<List<Movie>> {
        return try {
            val response=movieApi.searchMovies(query)
            Result.success(response
                .results
                .map {
                it.toDomain()
            })
        }
        catch (e: Exception){
            Result.failure(e.toNetworkError())
        }
    }

    override suspend fun getMovieTrailer(movieId: Int): MovieVideo? {
        val response=movieApi.getMovieVideos(movieId)
        return response.toTrailer()
    }

    override suspend fun getMovieCast(movieId: Int): List<CastMember> {
        val response=movieApi.getMovieCredits(movieId)
        return response.castToDomain()
    }

    override suspend fun getMovieCrew(movieId: Int): List<CrewMember> {
        val response=movieApi.getMovieCredits(movieId)
        return response.crewToDomain()
    }

    override suspend fun getSimilarMovies(movieId: Int): List<Movie> {
        val response=movieApi.getSimilarMovie(movieId)
        return response.results.map {
            it.toDomain()
        }
    }

    override suspend fun getGenres(): Result <List<Genre>> {
        return try {
            val response=movieApi.getMovieGenres()
            Result.success(response.genres.map {
                Genre(id = it.id, name = it.name)
            })
    }
        catch (e: Exception){
        Result.failure(e)
    }
    }

    override suspend fun getMovieByGenres(genreId: Int, page: Int): Result <List<Movie>> {
        return try {
           val response = movieApi.discoverMovies(genreId = genreId, page = page)
           Result.success(response.results.map {
               it.toDomain()
           })
       }
        catch (e: Exception){
            Result.failure(e)
        }}
    }
