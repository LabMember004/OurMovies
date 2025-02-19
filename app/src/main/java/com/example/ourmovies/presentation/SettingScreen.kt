package com.example.ourmovies.presentation

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ourmovies.domain.viewModels.UpdateEmailViewModel
import com.example.ourmovies.domain.viewModels.UpdatePasswordViewModel

@Composable
fun SettingsScreen(
    token: String,
    navController: NavController,
    profileViewModel: UpdateEmailViewModel = viewModel(),
    passwordViewModel: UpdatePasswordViewModel = viewModel()
) {
    var selectedOption by remember { mutableStateOf("View") } // Default view is the profile
    var email by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Access shared preferences
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for selecting update options
        Button(
            onClick = { selectedOption = "UpdateEmail" },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(text = "Update Email")
        }
        Button(
            onClick = { selectedOption = "UpdatePassword" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Password")
        }

        // Show update email form if selected
        if (selectedOption == "UpdateEmail") {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("New Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red)
            }

            Button(
                onClick = {
                    if (email.isNotEmpty() && token.isNotEmpty()) {
                        profileViewModel.updateEmail(token, email) { isSuccess, message ->
                            if (isSuccess) {
                                // Update shared preferences with the new email
                                sharedPreferences.edit().putString("USER_EMAIL", email).apply()
                                // Navigate back to profile page to reflect the changes
                                navController.navigate("profile")
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

        // Show update password form if selected
        if (selectedOption == "UpdatePassword") {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = { Text("Confirm New Password") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        passwordViewModel.updatePassword(
                            token = token,
                            currentPassword = currentPassword,
                            newPassword = newPassword,
                            confirmNewPassword = confirmNewPassword
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Update Password")
                }
            }
        }
    }
}

