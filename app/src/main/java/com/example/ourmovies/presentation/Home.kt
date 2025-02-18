package com.example.ourmovies.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.ourmovies.data.HighlightedMovie
import com.example.ourmovies.data.Movies
import com.example.ourmovies.data.Section
import com.example.ourmovies.domain.viewModels.MoviesViewModel

@Composable
fun Home(viewModel: MoviesViewModel = viewModel() ) {
    val sections by viewModel.sections.collectAsState()

    Log.d("Home Screen", "Sections: $sections")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Show a highlighted movie at the top
            if (sections.isNotEmpty() && sections.first().movies.isNotEmpty()) {
                HighlightedMovieSection(movie = sections.first().movies.first())
            }
        }

        items(sections) { section ->
            MovieSection(section)
        }
    }
}

@Composable
fun HighlightedMovieSection(movie: Movies , ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Featured Movie",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Image(
            painter = rememberImagePainter(movie.poster),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = movie.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(text = "⭐ ${movie.rating} | ${movie.genres.joinToString(", ")}")

        Button(
            onClick = {
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Watch Trailer")
        }
    }
}

@Composable
fun MovieSection(section: Section) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
    ) {
        Text(
            text = section.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(section.movies) { movie ->
                MovieCard(movie)
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movies) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(movie.poster),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = movie.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "⭐ ${movie.rating}",
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Text(
                text = movie.genres.joinToString(", "),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}
