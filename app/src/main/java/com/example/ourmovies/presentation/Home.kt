package com.example.ourmovies.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ourmovies.R
import com.example.ourmovies.data.HighlightedMovie
import com.example.ourmovies.data.Movies
import com.example.ourmovies.data.Section
import com.example.ourmovies.domain.viewModels.MoviesViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun Home(viewModel: MoviesViewModel = viewModel(), navController: NavController) {
    val sections by viewModel.sections.collectAsState()

    Log.d("Home Screen", "Sections: $sections")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            if (sections.isNotEmpty() && sections.first().movies.isNotEmpty()) {
                HighlightedMovieSection(movie = sections.first().movies.first())
            }
        }

        items(sections) { section ->
            MovieSection(section = section, navController = navController)
        }
    }
}




@Composable
fun HighlightedMovieSection(movie: Movies) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp )
    ) {
        Text(
            text = "Featured Movie",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        fun extractVideoId(url: String): String? {
            val regex = "https?://(?:www\\.)?youtube\\.com/(?:watch\\?v=|embed/)([A-Za-z0-9_-]+)".toRegex()
            val matchResult = regex.find(url)
            return matchResult?.groupValues?.get(1)
        }

        val videoId = movie.trailer?.let { extractVideoId(it) }

        if (videoId != null) {
            YoutubeScreen(videoId = videoId)
        } else {
            Image(
                painter = rememberImagePainter(movie.poster),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = movie.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(text = "⭐ ${movie.rating} | ${movie.genres.joinToString(", ")}")
    }
}



@Composable
fun YoutubeScreen(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    AndroidView(factory = {
        val view = YouTubePlayerView(ctx)
        view.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        )
        view
    })
}




@Composable
fun MovieSection(section: Section, navController: NavController) {
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
                HomeMovieCard(movie = movie, navController = navController)
            }
        }
    }
}

@Composable
fun HomeMovieCard(movie: Movies, navController: NavController) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable {
                navController.navigate("movieDetails/${movie._id}")
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .height(280.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = movie.poster,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movie.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Column {
                    Text(
                        text = "⭐ ${movie.rating}",
                        fontSize = 12.sp,
                    )

                    Text(
                        text = movie.genres.joinToString(", "),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}





