package com.example.ourmovies.domain.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.data.UpdateEmailRequest
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {  // Remove the nested class
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    fun setToken(newToken: String) {
        _token.value = newToken


    }




}
