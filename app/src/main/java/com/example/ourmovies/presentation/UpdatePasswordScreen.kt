package com.example.ourmovies.presentation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ourmovies.presentation.viewModels.UpdatePasswordViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton

@Composable
fun UpdatePasswordScreen(
    token: String,
    navController: NavController,
    passwordViewModel: UpdatePasswordViewModel = viewModel()
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Update Password", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Current Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it },
                label = { Text("Confirm New Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (successMessage.isNotEmpty()) {
                Text(text = successMessage, color = if (successMessage.startsWith("Success")) Color.Green else Color.Red)
            }

            Button(
                onClick = {
                    if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty() && token.isNotEmpty()) {
                        isLoading = true
                        passwordViewModel.updatePassword(
                            token,
                            currentPassword,
                            newPassword,
                            confirmNewPassword
                        ) { isSuccess, message ->
                            if (isSuccess) {
                                sharedPreferences.edit().putString("USER_PASSWORD", newPassword).apply()
                                successMessage = "Success: Password successfully changed"
                                navController.navigate("profile")
                            } else {
                                successMessage = "Error: $message"
                            }
                            isLoading = false
                        }
                    } else {
                        successMessage = "Error: All fields and token are required"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update Password")
            }
        }
    }
}
