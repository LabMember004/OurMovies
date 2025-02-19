package com.example.ourmovies.domain.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.launch

class DeleteViewModel : ViewModel() {


    fun deleteProfile(token: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.deleteProfile("Bearer $token")
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onError("Failed to delete profile")
            }
        }
    }
}