package com.example.ourmovies.data

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
data class RegisterResponse (
    val success: Boolean,
    val message: String,
)