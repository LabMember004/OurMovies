package com.example.ourmovies.domain.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.data.FavoriteRequest
import com.example.ourmovies.data.Movies
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class FavoriteViewModel : ViewModel() {
    private val _favorites = mutableStateOf<List<Movies>>(emptyList())
    val favorites: State<List<Movies>> get() = _favorites

    private val _delete = mutableStateOf<List<Movies>>(emptyList())
    val delete: State<List<Movies>> get() = _delete

    var isLoading by mutableStateOf(false)


    fun addFavoriteMovie(movieId: String, token: String) {
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
                        Log.e(
                            "MoviesViewModel",
                            "Failed to add movie: ${response.code()} - ${response.message()}"
                        )
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
                println("Error fetching favorites: ${e.message}")
                _favorites.value = emptyList()
            } finally {
                isLoading = false
            }
        }

    }


    fun deleteFavoriteMovie(favoriteId: String, token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteFavorite(favoriteId,"Bearer $token" , )
                Log.d("FavoriteViewModel", "Using token: Bearer $token")


                if (response.isSuccessful) {

                    _favorites.value = _favorites.value.filter { it._id != favoriteId }
                    Log.d("FavoriteViewModel", "Movie deleted successfully")
                } else {

                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("FavoriteViewModel", "Failed to delete movie: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error: ${e.message}")
            }
        }
    }
    fun isMovieFavorited(movieId: String): Boolean {
        return _favorites.value.any { it._id == movieId }
    }

}