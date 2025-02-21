package com.example.ourmovies.presentation.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    // Boolean state to store dark mode preference
    val isDarkMode = mutableStateOf(false)

    // Toggle the dark mode
    fun toggleTheme() {
        isDarkMode.value = !isDarkMode.value
    }
}
