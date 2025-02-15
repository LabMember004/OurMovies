package com.example.ourmovies.domain

import com.example.ourmovies.data.MoviesResponse

class Repository {

    suspend fun getMovieList(page: Int): MoviesResponse {
        val response = RetrofitInstance.api.getMovies(page)
        return response
    }
}

