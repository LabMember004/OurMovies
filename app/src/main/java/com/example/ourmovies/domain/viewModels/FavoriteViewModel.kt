package com.example.ourmovies.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.data.FavoriteRequest
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class FavoriteViewModel : ViewModel() {
    private val _favoriteStatus = MutableLiveData<Boolean>()
    val favoriteStatus: LiveData<Boolean> = _favoriteStatus

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun addToFavorites(movieId: String, token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.addToFavorites(
                    token = "Bearer $token",
                    request = FavoriteRequest(movieId)
                )

                if (response.isSuccessful) {
                    _favoriteStatus.value = true
                } else {
                    _error.value = "Failed to add to favorites"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}