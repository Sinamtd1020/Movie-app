package com.sinamtd.movieapp.data.repository


import com.sinamtd.movieapp.data.local.dao.FavoriteMovieDao
import com.sinamtd.movieapp.data.remote.mapper.toDomain
import com.sinamtd.movieapp.data.remote.mapper.toFavoriteEntity
import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteMovieDao
) : FavoriteRepository {

    override suspend fun addFavorite(movie: MovieDetail) {

        dao.insertFavorite(
            movie.toFavoriteEntity()
        )
    }

    override suspend fun removeFavorite(movieId: Int) {

        dao.deleteFavorite(movieId)
    }

    override fun getFavorites(): Flow<List<MovieDetail>> {

        return dao
            .getFavorites()
            .map { entities ->
                entities.map {
                    it.toDomain()
                }
            }
    }

    override fun isFavorite(
        movieId: Int
    ): Flow<Boolean> {

        return dao.isFavorite(movieId)
    }
}