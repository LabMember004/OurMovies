package com.example.ourmovies.domain.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ourmovies.data.RegisterRequest
import com.example.ourmovies.data.RegisterResponse
import com.example.ourmovies.domain.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isPasswordMatch by mutableStateOf(true)
    var errorMessage by mutableStateOf("")

    fun registerUser(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage = "All fields are required"
            return
        }

        if (password != confirmPassword) {
            isPasswordMatch = false
            errorMessage = "Passwords don't match"
            return
        }

        val registerRequest = RegisterRequest(name, email, password, confirmPassword)

        RetrofitInstance.api.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Log.d("RegisterViewModel", "Registration successful: ${response.body()}")
                    Log.d("RegisterRequest", "Name: ${registerRequest.name}, Email: ${registerRequest.email}, Password: ${registerRequest.password}, ConfirmPassword: ${registerRequest.confirmPassword}")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("RegisterViewModel", "Registration failed: $errorBody")
                    errorMessage = "Registration failed: $errorBody"
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("RegisterViewModel", "Failure: ${t.message}")
            }
        })
    }
}


