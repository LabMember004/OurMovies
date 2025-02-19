package com.example.ourmovies.domain.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.data.UpdateEmailRequest
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.launch

class UpdateEmailViewModel: ViewModel() {

    fun updateEmail(token: String, email: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                if (token.isNotEmpty()) {
                    val response = RetrofitInstance.api.updateEmail(
                        token = "Bearer $token",
                        request = UpdateEmailRequest(email)
                    )
                    if (response.isSuccessful) {
                        callback(true, "Email updated successfully!")
                    } else {

                        callback(false, "Failed to update email , new email cannot be the current email")
                    }
                } else {
                    callback(false, "Token is missing or invalid.")
                }
            } catch (e: Exception) {
                callback(false, "Network error: ${e.message}")
            }
        }
    }
}

