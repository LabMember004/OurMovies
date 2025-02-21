package com.example.ourmovies.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ourmovies.R
import com.example.ourmovies.data.Movies
import com.example.ourmovies.presentation.viewModels.AuthViewModel
import com.example.ourmovies.presentation.viewModels.FavoriteViewModel
import com.example.ourmovies.presentation.viewModels.MoviesViewModel

@Composable
fun MovieDetailsScreen(
    movieId: String,
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    moviesViewModel: MoviesViewModel = viewModel(),
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    val token by authViewModel.token.observeAsState("")
    val movieDetails by moviesViewModel.data.collectAsState()
    val movie = movieDetails.firstOrNull { it._id == movieId }

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {


            favoriteViewModel.getFavorites(token)
        }
    }

    if (movie != null) {

        MovieDetailsContent(movie, navController, favoriteViewModel, token)
    } else {
        MovieDetailsLoadingScreen()
    }
}

@Composable
fun MovieDetailsContent(
    movie: Movies,
    navController: NavController,
    viewModel: FavoriteViewModel,
    token: String
) {
    val favorites by viewModel.favorites.observeAsState(emptyList())
    val isFavorited = favorites.any { it._id == movie._id }
    val heartIconColor = if (isFavorited) Color.Red else Color.Gray

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = movie.title,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    if (token.isNotEmpty()) {
                        if (isFavorited) {
                            viewModel.deleteFavoriteMovie(movie._id, token)
                        } else {
                            viewModel.addFavoriteMovie(movie._id, token)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = heartIconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Release Year: ${movie.releaseYear}", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Rating: ${movie.rating}/10", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Runtime: ${movie.runtime} min", color = Color.Gray)
                Text(text = "Genres: ${movie.genres}", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
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

