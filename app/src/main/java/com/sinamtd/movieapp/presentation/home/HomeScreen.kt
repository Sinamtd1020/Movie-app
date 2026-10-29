package com.sinamtd.movieapp.presentation.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sinamtd.movieapp.R
import com.sinamtd.movieapp.domain.model.Genre
import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.presentation.genre.GenreUiState
import com.sinamtd.movieapp.presentation.genre.GenreViewModel
import com.sinamtd.movieapp.presentation.navigation.Screens
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,

    homeViewModel: HomeViewModel =
        hiltViewModel(),

    genreViewModel: GenreViewModel =
        hiltViewModel()
) {

    val uiState by
    homeViewModel.uiState.collectAsState()

    val genreUiState by
    genreViewModel.uiState.collectAsState()


    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("Movie App")
                }
            )
        }

    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            // --------------------------------
            // GENRES
            // --------------------------------

            if (
                genreUiState.isLoadingGenres
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(24.dp)
                    )
                }

            } else if (
                genreUiState.genres.isNotEmpty()
            ) {
                Text(text = "Genres", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp))

                GenreSelector(

                    genres =
                        genreUiState.genres,

                    selectedGenreId =
                        genreUiState.selectedGenreId,

                    onGenreSelected = { genreId ->
                        if (genreId==null){
                            genreViewModel.clearGenre()
                        }
                        else{
                        genreViewModel
                            .selectGenre(
                                genreId
                            )}
                    }
                )
            }


            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )


                 // --------------------------------
                 // CONTENT
                 // --------------------------------

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                // ----------------------------
                // Genre selected
                // ----------------------------

                if (
                    genreUiState.selectedGenreId != null
                ) {

                    GenreMoviesContent(

                        movies =
                            genreUiState.movies,

                        isLoading =
                            genreUiState.isLoadingMovies,

                        isLoadingMore =
                            genreUiState.isLoadingMore,

                        onLoadMore = {

                            genreViewModel
                                .loadNextPage()
                        },

                        onMovieClick = { movie ->

                            navController.navigate(

                                Screens.Detail
                                    .paramsWithArgs(
                                        movie.id.toString()
                                    )
                            )
                        }
                    )

                } else {

                    // ----------------------------
                    // Popular Movies
                    // ----------------------------

                    when (
                        val state = uiState
                    ) {

                        HomeUiState.Loading -> {

                            CircularProgressIndicator(
                                modifier =
                                    Modifier.align(
                                        Alignment.Center
                                    )
                            )
                        }


                        is HomeUiState.Error -> {

                            HomeError(message =
                                state.message,

                                onRetry = {

                                    homeViewModel
                                        .retry()
                                }
                            )
                        }


                        is HomeUiState.Success -> {

                            HomeContent(

                                movies =
                                    state.movies,

                                isLoadingMore =
                                    state.isLoadingMore,

                                onMovieClick = { movie ->

                                    navController.navigate(

                                        Screens.Detail
                                            .paramsWithArgs(
                                                movie.id.toString()
                                            )
                                    )
                                },

                                onLoadMore = {

                                    homeViewModel
                                        .loadNextPage()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeError(
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
private fun HomeContent(
    movies: List<Movie>,
    isLoadingMore: Boolean,
    onMovieClick: (Movie) -> Unit,
    onLoadMore: () -> Unit
) {

    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {

        snapshotFlow {
            gridState
                .layoutInfo
                .visibleItemsInfo
                .lastOrNull()
                ?.index
        }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->

                val totalItems =
                    gridState.layoutInfo.totalItemsCount

                if (
                    lastVisibleIndex != null &&
                    totalItems > 0 &&
                    lastVisibleIndex >= totalItems - 4 &&
                    !isLoadingMore
                ) {
                    onLoadMore()
                }
            }
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(
            items = movies,
            key = { movie ->
                movie.id
            }
        ) { movie ->

            MovieCard(
                movie = movie,
                onClick = {
                    onMovieClick(movie)
                }
            )
        }


        // Loading پایین لیست
        if (isLoadingMore) {

            item(
                span = {
                    GridItemSpan(
                        maxLineSpan
                    )
                }
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column {

            if (movie.poster_path.isNullOrBlank()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        painter = painterResource(R.drawable.video),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }

            } else {

                AsyncImage(
                    model =
                        "https://image.tmdb.org/t/p/w500${movie.poster_path}",

                    contentDescription =
                        movie.title,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),

                    contentScale =
                        ContentScale.Crop
                )
            }

            Text(
                text = movie.title,
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
private fun GenreSelector(
    genres: List<Genre>,
    selectedGenreId: Int?,
    onGenreSelected: (Int?) -> Unit
) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // ALL
        item {

            FilterChip(
                selected = selectedGenreId == null,

                onClick = {
                    onGenreSelected(null)
                },

                label = {
                    Text("All")
                }
            )
        }

        // GENRES
        items(
            items = genres,
            key = { it.id }
        ) { genre ->

            FilterChip(
                selected =
                    selectedGenreId == genre.id,

                onClick = {
                    onGenreSelected(genre.id)
                },

                label = {
                    Text(genre.name)
                }
            )
        }
    }
}

@Composable
private fun GenreMoviesContent(
    movies: List<Movie>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onMovieClick: (Movie) -> Unit
) {

    if (isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment =
                Alignment.Center
        ) {

            CircularProgressIndicator()
        }

        return
    }


    if (movies.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = "No movies found"
            )
        }

        return
    }


    val gridState =
        rememberLazyGridState()


    LaunchedEffect(gridState) {

        snapshotFlow {

            gridState
                .layoutInfo
                .visibleItemsInfo
                .lastOrNull()
                ?.index

        }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->

                val totalItems =
                    gridState
                        .layoutInfo
                        .totalItemsCount

                if (
                    lastVisibleIndex != null &&
                    totalItems > 0 &&
                    lastVisibleIndex >=
                    totalItems - 4 &&
                    !isLoadingMore
                ) {

                    onLoadMore()
                }
            }
    }


    LazyVerticalGrid(

        columns =
            GridCells.Fixed(2),

        state =
            gridState,

        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(12.dp),

        horizontalArrangement =
            Arrangement.spacedBy(12.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)

    ) {


        items(

            items = movies,

            key = { movie ->
                movie.id
            }

        ) { movie ->


            MovieCard(

                movie = movie,

                onClick = {
                    onMovieClick(movie)
                }
            )
        }


        if (isLoadingMore) {

            item(

                span = {

                    GridItemSpan(
                        maxLineSpan
                    )
                }

            ) {

                Box(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    contentAlignment =
                        Alignment.Center

                ) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}