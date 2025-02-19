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

@Composable
fun UpdateEmailScreen(
    token: String,
    navController: NavController,
    profileViewModel: UpdateEmailViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") } // Success message state

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
        Text(text = "Update Email", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

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

        if (successMessage.isNotEmpty()) {
            Text(text = successMessage, color = Color.Green) // Display success message
        }

        Button(
            onClick = {
                if (email.isNotEmpty() && token.isNotEmpty()) {
                    profileViewModel.updateEmail(token, email) { isSuccess, message ->
                        if (isSuccess) {
                            // Update shared preferences with the new email
                            sharedPreferences.edit().putString("USER_EMAIL", email).apply()
                            // Set success message
                            successMessage = "Email successfully changed"
                            // Navigate back to the profile page
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
}

