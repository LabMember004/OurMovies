package com.example.ourmovies.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ourmovies.domain.ProfileImagePicker
import com.example.ourmovies.domain.navBar.NavDestination
import com.example.ourmovies.domain.viewModels.AuthViewModel
import com.example.ourmovies.domain.viewModels.UpdateEmailViewModel

@Composable
fun Profile(
    viewModel: UpdateEmailViewModel = viewModel(),
    token: String,

) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }





    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Update Profile", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("New Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }

        Button(
            onClick = {
                if (email.isNotEmpty() && token.isNotEmpty()) {
                    isLoading = true
                    viewModel.updateEmail(token, email) { isSuccess, message ->
                        isLoading = false
                        if (isSuccess) {
                        } else {
                            errorMessage = message
                        }
                    }
                } else {
                    errorMessage = "Email and token are required"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Email")
        }
    }
}

