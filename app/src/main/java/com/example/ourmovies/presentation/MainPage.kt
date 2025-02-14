package com.example.ourmovies.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.ourmovies.domain.viewModels.MoviesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ourmovies.data.Movies

@Composable
fun MainPage(viewModel: MoviesViewModel = viewModel(), navController: NavController) {
    val data by viewModel.data.collectAsState()
    val isLoading = viewModel.isLoading
    val isEndReached = viewModel.isEndReached

    val listState = rememberLazyListState()

    LaunchedEffect(listState.firstVisibleItemIndex) {
        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
        val totalItems = listState.layoutInfo.totalItemsCount

        if (lastVisibleItem != null && lastVisibleItem.index == totalItems - 1 && !isLoading && !isEndReached) {
            viewModel.loadNextPage()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Button to navigate to FavoriteMoviesScreen
        Button(
            onClick = {
                // Navigate to the FavoriteMoviesScreen
                navController.navigate("FavoriteMoviesScreen")
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Go to Favorite Movies")
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.padding(16.dp)
        ) {
            items(data) { item: Movies ->
                MovieCard(movie = item, navController)
            }

            if (isLoading) {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}


@Composable
fun MovieCard(movie: Movies, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable {
                navController.navigate("movieDetails/${movie._id}")
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(movie.poster),
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = movie.title, modifier = Modifier.padding(bottom = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Year: ${movie.releaseYear}")
                Text(text = "Rating: ${movie.rating}")
            }
            Text(text = "Runtime: ${movie.runtime} min")


        }
    }
}
