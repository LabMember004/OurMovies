package com.example.ourmovies.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ourmovies.data.Movies
import com.example.ourmovies.domain.viewModels.FavoriteViewModel
import com.example.ourmovies.domain.viewModels.MoviesViewModel

@Composable
fun FavoriteMoviesScreen(
    viewModel: FavoriteViewModel = viewModel(),
    token: String,
    navController: NavController
) {
    val favorites = viewModel.favorites.value





    println("Favorites state: $favorites")

    LaunchedEffect(Unit) {
        if (token.isNotEmpty()) {
            println("Fetching favorites with token: $token")

            viewModel.getFavorites(token)
        }
    }


    IconButton(onClick = { navController.popBackStack() }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
    }


    LazyColumn {
        items(favorites) { movie ->
            MovieItem(movie , viewModel ,token)
        }
    }
}

@Composable
fun MovieItem(movie: Movies , viewModel: FavoriteViewModel, token: String) {


    Column(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = rememberImagePainter(movie.poster), // Replace with the actual image URL or resource
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movie.title,
        )

        Text(
            text = "Release Date: ${movie.releaseYear}",
            color = Color.Gray
        )
        Button(
            onClick = {
                viewModel.deleteFavoriteMovie(movie._id , token)
            }
        ) {
            Text("Delete")
        }
    }


}
