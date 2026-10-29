package com.sinamtd.movieapp.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.presentation.navigation.Screens

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val query by viewModel.query.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = {
                viewModel.onQueryChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            placeholder = {
                Text("Search movies...")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { viewModel.searchMovies(query)})
        )

        when (val state = uiState) {

            SearchUiState.Idle -> {

                SearchIdle()
            }

            SearchUiState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }

            is SearchUiState.Success -> {

                if (state.movies.isEmpty()) {

                    SearchEmpty()

                } else {

                    SearchResults(
                        movies = state.movies,
                        navController = navController
                    )
                }
            }

            is SearchUiState.Error -> {

                SearchError(
                    message = state.message,
                    onRetry = {
                        viewModel.retry(query)
                    }
                )
            }
        }
    }
}
@Composable
private fun SearchMovieCard(
    movie: Movie,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column {

            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = movie.title,
                modifier = Modifier.padding(8.dp),
                maxLines = 1
            )
        }
    }
}
@Composable
private fun SearchIdle()
{

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Search for a movie"
        )
    }
}
@Composable
private fun SearchEmpty() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "No movies found",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Try searching with a different title.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
private fun SearchError(
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
@Composable
private fun SearchResults(
    movies: List<Movie>,
    navController: NavController
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(
            items = movies,
            key = { it.id }
        ) { movie ->

            SearchMovieCard(
                movie = movie,
                onClick = {

                    navController.navigate(
                        Screens.Detail.paramsWithArgs(
                            movie.id.toString()
                        )
                    )
                }
            )
        }
    }
}