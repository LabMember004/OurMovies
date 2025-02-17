package com.example.ourmovies.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    LaunchedEffect(Unit) {
        if (token.isNotEmpty()) {
            viewModel.getFavorites(token)
        }
    }

    IconButton(onClick = { navController.popBackStack() }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
    }

    LazyColumn {
        items(favorites) { movie ->
            MovieItem(movie, viewModel, token)
        }
    }
}

@Composable
fun MovieItem(movie: Movies, viewModel: FavoriteViewModel, token: String) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title,
                )

                Text(
                    text = "Release Date: ${movie.releaseYear}",
                    color = Color.Gray,
                )
            }

            IconButton(
                onClick = { viewModel.deleteFavoriteMovie(movie._id, token) },
                modifier = Modifier.size(32.dp) // Make the icon bigger
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

