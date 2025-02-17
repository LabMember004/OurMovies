package com.example.ourmovies.presentation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ourmovies.domain.ProfileImagePicker
import com.example.ourmovies.domain.navBar.NavDestination

@Composable
fun Profile(context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userName = sharedPreferences.getString("USER_NAME", "Unknown User") ?: "Unknown User"
    val userEmail = sharedPreferences.getString("USER_EMAIL", "Unknown Email") ?: "Unknown Email"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImagePicker(context)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Name: $userName")
        Text(text = "Email: $userEmail")
    }
}