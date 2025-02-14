package com.example.ourmovies.data

data class MoviesResponse(
    val totalDocs: Int,
    val totalPages: Int,
    val currentPage: Int,
    val data : List<Movies>
)
