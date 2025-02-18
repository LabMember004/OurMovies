package com.example.ourmovies.domain

import com.example.ourmovies.data.MoviesResponse

class Repository {

    suspend fun getMovieList(page: Int ,genre: String? = null, releaseYear: Int? = null, query: String? = null ): MoviesResponse {
        val response = RetrofitInstance.api.getMovies(page , genre , releaseYear,query)
        return response
    }
}

