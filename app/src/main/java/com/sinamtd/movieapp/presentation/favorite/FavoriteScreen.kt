package com.sinamtd.movieapp.presentation.favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.presentation.navigation.Screens


private const val IMAGE_BASE_URL =
    "https://image.tmdb.org/t/p/w500"

@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FavoriteViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        when (val state = uiState) {

            FavoriteUiState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }


            is FavoriteUiState.Error -> {

                FavoriteError(
                    message = state.message,
                    onRetry = {
                        viewModel.retry()
                    }
                )
            }


            is FavoriteUiState.Success -> {

                if (state.movies.isEmpty()) {

                    FavoriteEmpty()

                } else {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        items(
                            items = state.movies,
                            key = { movie ->
                                movie.id
                            }
                        ) { movie ->

                            FavoriteMovieCard(
                                movie = movie,
                                onClick = {

                                    navController.navigate(
                                        Screens.Detail
                                            .paramsWithArgs(
                                                movie.id.toString()
                                            )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
private fun FavoriteMovieCard(
    movie: MovieDetail,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column {

            AsyncImage(
                model = IMAGE_BASE_URL + movie.poster_path,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = movie.title,
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Text(
                    text = String.format(
                        "%.1f",
                        movie.vote_average
                    )
                )
            }
        }
    }
}

@Composable
private fun FavoriteEmpty() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "No favorite movies",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Add movies to your favorites.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun FavoriteError(
    message: String,
    onRetry: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = message
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                onClick = onRetry
            ) {

                Text("Retry")
            }
        }
    }
}