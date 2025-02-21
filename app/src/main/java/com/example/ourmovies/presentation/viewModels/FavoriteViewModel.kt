package com.example.ourmovies.presentation.viewModels

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

class FavoriteViewModel() : ViewModel() {
    private val _favorites = MutableLiveData<List<Movies>>(emptyList())
    val favorites: LiveData<List<Movies>> get() = _favorites

    var isLoading by mutableStateOf(false)

    fun addFavoriteMovie(movieId: String, token: String) {
        viewModelScope.launch {
            try {
                if (token.isNotEmpty()) {
                    val favoriteRequest = FavoriteRequest(movieId)
                    Log.d("FavoriteViewModel", "Sending request: $favoriteRequest")

                    val response = RetrofitInstance.api.addToFavorites(
                        token = "Bearer $token",
                        request = favoriteRequest
                    )

                    if (response.isSuccessful) {
                        Log.d("FavoriteViewModel", "Movie added to favorites successfully")
                        getFavorites(token)
                    } else {
                        Log.e("FavoriteViewModel", "Failed to add favorite: ${response.code()} - ${response.message()}")
                        Log.e("FavoriteViewModel", "Error body: ${response.errorBody()?.string() ?: "No error body"}")
                    }
                } else {
                    Log.e("FavoriteViewModel", "Token is missing or invalid.")
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error: ${e.message}")
            }
        }
    }

    fun getFavorites(token: String) {
        if (token.isEmpty()) {
            Log.e("FavoriteViewModel", "Token is empty, cannot fetch favorites.")
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getFavorites("Bearer $token")
                if (response.isSuccessful) {
                    val favoriteList = response.body() ?: emptyList()
                    Log.d("FavoriteViewModel", "Fetched favorites: $favoriteList")
                    _favorites.postValue(favoriteList)
                } else {
                    Log.e("FavoriteViewModel", "Failed to fetch favorites: ${response.code()} - ${response.message()}")
                    _favorites.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error fetching favorites: ${e.message}")
                _favorites.postValue(emptyList())
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteFavoriteMovie(favoriteId: String, token: String) {
        if (token.isEmpty()) {
            Log.e("FavoriteViewModel", "Token is empty, cannot delete favorite.")
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteFavorite(favoriteId, "Bearer $token")
                Log.d("FavoriteViewModel", "Using token: Bearer $token")

                if (response.isSuccessful) {
                    Log.d("FavoriteViewModel", "Movie deleted successfully")
                    getFavorites(token)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("FavoriteViewModel", "Failed to delete movie: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error deleting favorite: ${e.message}")
            }
        }
    }

    fun isMovieFavorited(movieId: String): Boolean {
        return favorites.value?.any { it._id == movieId } ?: false
    }
}
