package com.example.ourmovies.data

data class HighlightedMovie(
    val _id: String,
    val title: String,
    val tagline: String,
    val plot: String,
    val releaseYear: Int,
    val poster: String,
    val rating : Double,
    val runtime: Int,
    val genres: List<String>,
    val trailer: String


)
