package com.example.ourmovies.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ourmovies.AuthViewModel
import com.example.ourmovies.data.Movies
import com.example.ourmovies.domain.viewModels.MoviesViewModel


@Composable
fun MovieDetailsScreen(
    movieId: String,
    navController: NavController,
    authViewModel: AuthViewModel,
    moviesViewModel: MoviesViewModel = viewModel() // Use the MoviesViewModel here
) {
    // Observe the token from AuthViewModel
    val token by authViewModel.token.observeAsState("")

    // Observe the movie details from MoviesViewModel
    val movieDetails by moviesViewModel.data.collectAsState()

    // Find the movie based on movieId
    val movie = movieDetails.firstOrNull { it._id == movieId }

    // Display content if movie is available, otherwise show loading screen
    if (movie != null) {
        MovieDetailsContent(movie, navController, moviesViewModel, token)
    } else {
        MovieDetailsLoadingScreen()
    }
}

@Composable
fun MovieDetailsContent(
    movie: Movies,
    navController: NavController,
    viewModel: MoviesViewModel,
    token: String
) {
    Column(modifier = Modifier.padding(16.dp)) {

        // Movie Poster
        Image(
            painter = rememberImagePainter(movie.poster),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Title
        Text(
            text = movie.title,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Add to Favorites Button
        Button(
            onClick = {
                // Ensure the token is not empty before making the request
                if (token.isNotEmpty()) {
                    viewModel.addFavoriteMovie(movie._id, token) // Call ViewModel function to add movie to favorites
                }
            },
        ) {
            Text("Add to Favorites")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Release Date
        Text(
            text = "Release Year: ${movie.releaseYear}",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Back Button
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun MovieDetailsLoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Loading movie details...")
    }
}
