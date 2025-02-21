package com.example.ourmovies.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.ourmovies.data.UpdatePasswordRequest
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.launch

class UpdatePasswordViewModel: ViewModel() {

    private val _updatePasswordResult = mutableStateOf<Result>(Result.Loading)
    val updatePasswordResult: State<Result> get() = _updatePasswordResult

    sealed class Result {
        object Loading : Result()
        data class Success(val message: String) : Result()
        data class Error(val message: String) : Result()
    }

    fun updatePassword(
        token: String,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        callback: (Boolean,String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // If token is not empty, proceed with the API call
                if (token.isNotEmpty()) {
                    _updatePasswordResult.value = Result.Loading // Set loading state

                    // Perform the API call
                    val response = RetrofitInstance.api.updatePassword(
                        token = "Bearer $token",
                        request = UpdatePasswordRequest(
                            currentPassword,
                            newPassword,
                            confirmNewPassword
                        )
                    )

                    // Log response for debugging
                    Log.d("UpdatePassword", "Response: ${response.body()}")
                    Log.d("UpdatePassword", "Response code: ${response.code()}")
                    Log.d("UpdatePassword", "Response message: ${response.message()}")

                    if (response.isSuccessful) {
                        _updatePasswordResult.value =
                            Result.Success("Password updated successfully.")
                        callback(true, "Password updated successfully.")

                    } else {
                        _updatePasswordResult.value =
                            Result.Error("Failed to update password. Code: ${response.code()}")
                    }
                } else {
                    _updatePasswordResult.value = Result.Error("Token is required.")
                }
            } catch (e: Exception) {
                _updatePasswordResult.value = Result.Error("Network error: ${e.message}")
                Log.d("Error", "Network error ${e.message}")
            }
        }
    }
}
