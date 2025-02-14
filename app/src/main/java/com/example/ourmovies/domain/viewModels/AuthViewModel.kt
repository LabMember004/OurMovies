package com.example.ourmovies.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {  // Remove the nested class
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    fun setToken(newToken: String) {
        _token.value = newToken
    }
}
