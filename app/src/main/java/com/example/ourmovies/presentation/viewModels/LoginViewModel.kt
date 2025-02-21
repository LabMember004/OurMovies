package com.example.ourmovies.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.data.LoginRequest
import com.example.ourmovies.data.LoginResponse
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    var errorMessage by mutableStateOf("")
    var token: String? by mutableStateOf(null)
    var isLoading by mutableStateOf(false)

    fun loginUser(email: String, password: String, onResult: (String?) -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage = "All fields are required"
            onResult(null)
            return
        }

        isLoading = true

        val loginRequest = LoginRequest(email, password)

        RetrofitInstance.api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                isLoading = false
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    token = loginResponse?.token
                    Log.d("LoginViewModel", "Login Successful: Token = $token")
                    onResult(token)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    errorMessage = "Login failed: $errorBody"
                    Log.e("LoginViewModel", "Login failed: $errorBody")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading = false
                errorMessage = "Network error: ${t.message}"
                Log.e("LoginViewModel", "Failure: ${t.message}")
                onResult(null)
            }
        })
    }

}