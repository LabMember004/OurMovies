package com.example.ourmovies.domain.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.data.FavoriteRequest
import com.example.ourmovies.data.Movies
import com.example.ourmovies.data.MoviesResponse
import com.example.ourmovies.domain.Repository
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel: ViewModel() {
    private val repository = Repository()
    private val _data = MutableStateFlow<List<Movies>>(emptyList())
    private val _paginationData = MutableStateFlow<MoviesResponse?>(null)
    val data: StateFlow<List<Movies>> = _data.asStateFlow()

    private val _favorites = mutableStateOf<List<Movies>>(emptyList())
    val favorites: State<List<Movies>> get() = _favorites


    private var currentPage = 1
    var isLoading by mutableStateOf(false)
    var isEndReached by mutableStateOf(false)



    init {
        fetchData(currentPage)
    }

    private fun fetchData(page: Int) {
        if (isLoading || isEndReached) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = repository.getMovieList(page)
                if (response.data.isNotEmpty()) {
                    _data.value += response.data
                    currentPage = page
                    isEndReached = currentPage >= response.totalPages
                }
                _paginationData.value = response
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }

    fun loadNextPage() {
        if (!isEndReached) {
            fetchData(currentPage + 1)
        }
    }

    fun addFavoriteMovie(movieId: String, token: String) {
        Log.d("MoviesViewModel", "addFavoriteMovie called with movieId: $movieId and token: $token")
        viewModelScope.launch {
            try {
                if (token.isNotEmpty()) {
                    // Log the request body
                    val favoriteRequest = FavoriteRequest(movieId)
                    Log.d("MoviesViewModel", "Sending request: $favoriteRequest")

                    val response = RetrofitInstance.api.addToFavorites(
                        token = "Bearer $token",
                        request = favoriteRequest
                    )

                    if (response.isSuccessful) {
                        Log.d("MoviesViewModel", "Movie added to favorites successfully")
                        getFavorites(token)
                    } else {
                        Log.e("MoviesViewModel", "Failed to add movie: ${response.code()} - ${response.message()}")
                        val errorBody = response.errorBody()?.string() ?: "No error body"
                        Log.e("MoviesViewModel", "Error body: $errorBody")
                    }
                } else {
                    Log.e("MoviesViewModel", "Token is missing or invalid.")
                }
            } catch (e: Exception) {
                Log.e("MoviesViewModel", "Error: ${e.message}")
            }
        }
    }




    fun getFavorites(token: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getFavorites("Bearer $token")
                if (response.isSuccessful) {
                    val favoriteList = response.body() ?: emptyList()
                    println("Favorites response: $favoriteList")
                    _favorites.value = favoriteList
                } else {
                    println("Failed to fetch favorites: ${response.code()}")
                    _favorites.value = emptyList()
                }
            } catch (e: Exception) {
                println("Error fetching favorites: ${e.message}")  // Log any exceptions
                _favorites.value = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

}






