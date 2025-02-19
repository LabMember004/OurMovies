package com.example.ourmovies.presentation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ourmovies.domain.ProfileImagePicker
import com.example.ourmovies.domain.viewModels.UpdateEmailViewModel
import com.example.ourmovies.domain.viewModels.UpdatePasswordViewModel

@Composable
fun Profile(
    navController: NavController
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Observe the email and username from shared preferences
    val userEmail = remember {
        mutableStateOf(sharedPreferences.getString("USER_EMAIL", "Unknown User") ?: "Unknown User")
    }
    val userName = sharedPreferences.getString("USER_NAME", "Unknown User") ?: "Unknown User"

    // Trigger recomposition when sharedPreferences change
    LaunchedEffect(userEmail.value) {
        userEmail.value = sharedPreferences.getString("USER_EMAIL", "Unknown User") ?: "Unknown User"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Gear Icon Button for navigating to the settings page at the top right
        IconButton(
            onClick = { navController.navigate("setting") },
            modifier = Modifier
                .align(Alignment.TopEnd) // Align the icon at the top right
                .padding(16.dp) // Optional padding to provide space around the button
        ) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
        }

        // Main content below the gear icon
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile UI (name, email, profile image)
            Text(text = "Profile", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Profile Image Picker (Placeholder for profile image)
            ProfileImagePicker(context)

            Spacer(modifier = Modifier.height(16.dp))

            // Display name and email
            Text(text = "Name: $userName")
            Text(text = "Email: ${userEmail.value}")

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



