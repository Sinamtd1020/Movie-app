package com.sinamtd.movieapp.presentation.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sinamtd.movieapp.domain.model.CastMember
import com.sinamtd.movieapp.domain.model.Movie
import com.sinamtd.movieapp.domain.model.MovieDetail
import com.sinamtd.movieapp.presentation.navigation.Screens

private const val  IMAGE_BASE_URL ="https://image.tmdb.org/t/p/w500"

private const val  BACKDROP_BASE_URL ="https://image.tmdb.org/t/p/w780"

@Composable
fun DetailScreen(movieId: String, navController: NavController,viewModel: DetailViewModel= hiltViewModel()) {
    val uiState by
    viewModel.uiState.collectAsState()

    val isFavorite by
    viewModel.isFavorite.collectAsState()

    val trailerState by
    viewModel.trailerState.collectAsState()


    val creditsState by
    viewModel.creditState.collectAsState()

    val similarMoviesState by
    viewModel.similarMoviesState.collectAsState()

    LaunchedEffect(movieId) {
        val id=movieId.toIntOrNull()
        if (id!=null){
        viewModel.getMovieDetails(id)}

    }

    Box(modifier = Modifier.fillMaxSize()){

    when(val state=uiState) {
        MovieDetailUiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is MovieDetailUiState.Error -> {
           MovieDetailError(message = state.message, onRetry = {movieId.toIntOrNull()?.let { id ->
               viewModel.retry(id)
           }})
        }

        is MovieDetailUiState.Success -> {
            MovieDetailContent(movie = state.movie, isFavorite = isFavorite, onFavoriteClick = {viewModel.toggleFavorite(state.movie)}
                ,onBackClick = {navController.popBackStack()}, trailerState = trailerState,
                onWatchTrailer =  { key ->

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://www.youtube.com/watch?v=$key"
                        )
                    )

                    navController.context.startActivity(intent)
                },
                creditsState = creditsState, similarMoviesState = similarMoviesState,
                onSimilarMovieClick = {id->
                    navController.navigate(Screens.Detail.paramsWithArgs(id.toString()))
                })

        }
    }
}

}
@Composable
private fun MovieDetailContent(
    movie: MovieDetail,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    trailerState: TrailerUiState,
    onWatchTrailer: (String) -> Unit,
    creditsState: CreditUiState,
    similarMoviesState: SimilarMovieUiState,
    onSimilarMovieClick: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {

        // --------------------------------
        // BACKDROP
        // --------------------------------

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {

            AsyncImage(
                model =
                    BACKDROP_BASE_URL +
                            movie.backdrop_path,

                contentDescription =
                    movie.title,

                modifier = Modifier
                    .fillMaxSize(),

                contentScale =
                    ContentScale.Crop
            )


            // Back Button

            IconButton(
                onClick = onBackClick,

                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color =
                            MaterialTheme
                                .colorScheme
                                .surface
                                .copy(alpha = 0.7f),

                        shape =
                            RoundedCornerShape(50)
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.ArrowBack,

                    contentDescription =
                        "Back"
                )
            }


            // Favorite Button

            IconButton(
                onClick =
                    onFavoriteClick,

                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color =
                            MaterialTheme
                                .colorScheme
                                .surface
                                .copy(alpha = 0.7f),

                        shape =
                            RoundedCornerShape(50)
                    )
            ) {

                Icon(
                    imageVector =
                        if (isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },

                    contentDescription =
                        "Favorite"
                )
            }
        }


        // --------------------------------
        // BASIC INFO
        // --------------------------------

        MovieBasicInfo(
            movie = movie
        )


        // --------------------------------
        // GENRES
        // --------------------------------

        movie.genres?.let { genres ->

            MovieGenres(
                genres = genres.map {
                    it.name
                }
            )
        }


        // --------------------------------
        // TRAILER
        // --------------------------------

        TrailerSection(
            state = trailerState,
            onWatchTrailer = onWatchTrailer
        )


        // --------------------------------
        // OVERVIEW
        // --------------------------------

        MovieOverview(
            overview = movie.overview
        )


        // --------------------------------
        // RUNTIME
        // --------------------------------

        MovieRuntime(
            runtime = movie.runtime
        )


        // --------------------------------
        // CAST
        // --------------------------------

        CastSection(
            state = creditsState
        )


        // --------------------------------
        // CREW
        // --------------------------------
        CrewSection(
            state = creditsState
        )


        // --------------------------------
        // SIMILAR MOVIES
        // --------------------------------

        SimilarMoviesSection(
            state = similarMoviesState,

            onMovieClick =
                onSimilarMovieClick
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}
@SuppressLint("DefaultLocale")
@Composable
private fun MovieBasicInfo(
    movie: MovieDetail
) {
    val imageUrl=movie.poster_path
        ?.takeIf { it.isNotBlank() }
        ?.let { IMAGE_BASE_URL+it }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (movie.poster_path.isNullOrBlank()){
            Box(modifier = Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center){
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "", modifier = Modifier.size(48.dp))
            }
        }
         else {
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    ),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Text(
                    text = String.format(
                        "%.1f / 10",
                        movie.vote_average
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            movie.release_date?.let {

                Text(
                    text = it
                )
            }
        }
    }
}

@Composable
private fun MovieGenres(
    genres: List<String>
) {

    if (genres.isEmpty()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp
        )
    ) {

        genres.take(3).forEach { genre ->

            Text(
                text = genre,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
            )
        }
    }
}

@Composable
private fun MovieOverview(
    overview: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = if (overview.isBlank()) {
                "No overview available."
            } else {
                overview
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun MovieRuntime(
    runtime: Int?
) {

    if (runtime == null) return

    Text(
        text = "Runtime: $runtime min",
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
        style = MaterialTheme.typography.bodyLarge
    )
}
@Composable
private fun MovieDetailError(
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
private fun TrailerSection(
    state: TrailerUiState,
    onWatchTrailer: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
    ) {

        Text(
            text = "Trailer",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        when {

            // -------------------------
            // Loading
            // -------------------------

            state.isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }


            // -------------------------
            // Error
            // -------------------------

            state.error != null -> {

                Text(
                    text = "Unable to load trailer",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }


            // -------------------------
            // Trailer موجود است
            // -------------------------

            state.trailer != null -> {

                val trailer = state.trailer

                Card(
                    onClick = {
                        onWatchTrailer(trailer.key)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                    ) {

                        AsyncImage(
                            model =
                                "https://img.youtube.com/vi/${trailer.key}/hqdefault.jpg",

                            contentDescription =
                                trailer.name,

                            modifier = Modifier.fillMaxSize(),

                            contentScale =
                                ContentScale.Crop
                        )


                        // Overlay

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme
                                        .colorScheme
                                        .scrim
                                        .copy(alpha = 0.25f)
                                )
                        )


                        // Play Button

                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp)
                                .background(
                                    MaterialTheme
                                        .colorScheme
                                        .primary,
                                    CircleShape
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.PlayArrow,

                                contentDescription =
                                    "Watch trailer",

                                tint =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary,
                                modifier =
                                    Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = trailer.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }


            // -------------------------
            // Trailer وجود ندارد
            // -------------------------

            else -> {

                Text(
                    text = "No trailer available",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun CastSection(
    state: CreditUiState
) {

    // اگر نه loading داریم، نه cast، چیزی نمایش نده
    if (
        !state.isLoading &&
        state.cast.isEmpty() &&
        state.error == null
    ) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {

        Text(
            text = "Cast",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Loading
        if (state.isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }

            return@Column
        }

        // Error
        if (state.error != null) {

            Text(
                text = "Unable to load cast",
                modifier = Modifier.padding(
                    horizontal = 16.dp
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )

            return@Column
        }

        // Cast
        LazyRow(
            contentPadding =
                PaddingValues(horizontal = 16.dp),

            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = state.cast.take(10),
                key = { cast ->
                    cast.id
                }
            ) { cast ->

                CastCard(
                    cast = cast
                )
            }
        }
    }
}

@Composable
private fun CastCard(
    cast: CastMember
) {

    Column(
        modifier = Modifier.width(110.dp)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp)
        ) {

            if (cast.profilePath.isNullOrBlank()) {

                // -------------------------
                // Placeholder
                // -------------------------

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Person,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(48.dp)
                    )
                }

            } else {

                // -------------------------
                // Actor Image
                // -------------------------

                AsyncImage(
                    model =
                        IMAGE_BASE_URL +
                                cast.profilePath,

                    contentDescription =
                        cast.name,

                    modifier =
                        Modifier.fillMaxSize(),

                    contentScale =
                        ContentScale.Crop
                )
            }
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = cast.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = cast.character.ifBlank {
                "Unknown character"
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
@Composable
private fun SimilarMoviesSection(
    state: SimilarMovieUiState,
    onMovieClick: (Int) -> Unit
) {

    // هیچ داده‌ای نداریم و در حال loading هم نیستیم
    if (
        !state.isLoading &&
        state.movies.isEmpty() &&
        state.error == null
    ) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {

        Text(
            text = "Similar Movies",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        when {

            // -------------------------
            // Loading
            // -------------------------

            state.isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }


            // -------------------------
            // Error
            // -------------------------

            state.error != null -> {

                Text(
                    text = "Unable to load similar movies",
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }


            // -------------------------
            // Empty
            // -------------------------

            state.movies.isEmpty() -> {

                Text(
                    text = "No similar movies available.",
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            // -------------------------
            // Movies
            // -------------------------

            else -> {

                LazyRow(
                    contentPadding =
                        PaddingValues(
                            horizontal = 16.dp
                        ),

                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = state.movies
                            .distinctBy { it.id }
                            .take(10),

                        key = { movie ->
                            movie.id
                        }
                    ) { movie ->

                        SimilarMovieCard(
                            movie = movie,
                            onClick = {
                                onMovieClick(movie.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun SimilarMovieCard(
    movie: Movie,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.width(130.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {

                if (movie.poster_path.isNullOrBlank()) {

                    // -------------------------
                    // Poster Placeholder
                    // -------------------------

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Info,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(48.dp)
                        )
                    }

                } else {

                    AsyncImage(
                        model =
                            IMAGE_BASE_URL +
                                    movie.poster_path,

                        contentDescription =
                            movie.title,

                        modifier =
                            Modifier.fillMaxSize(),

                        contentScale =
                            ContentScale.Crop
                    )
                }
            }

            Text(
                text = movie.title,
                modifier = Modifier.padding(8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CrewSection(
    state: CreditUiState
) {

    if (
        !state.isLoading &&
        state.crew.isEmpty()&&
        state.error==null
    ) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Crew",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if (state.isLoading) {

            CircularProgressIndicator()

        } else {

            val importantCrew =
                state.crew.filter {

                    it.job.equals(
                        "Director",
                        ignoreCase = true
                    ) ||
                            it.job.equals(
                                "Writer",
                                ignoreCase = true
                            ) ||
                            it.job.equals(
                                "Screenplay",
                                ignoreCase = true
                            )
                }

            importantCrew
                .distinctBy {
                    "${it.id}-${it.job}"
                }
                .take(5)
                .forEach { member ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {

                        Text(
                            text = "${member.job}: ",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
        }
    }
}