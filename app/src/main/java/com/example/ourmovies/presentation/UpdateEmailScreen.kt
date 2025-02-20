package com.example.ourmovies.presentation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var message by remember { mutableStateOf("") }
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
            Text(text = "Update Email", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("New Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (message.isNotEmpty()) {
                val messageColor = if (message.startsWith("Success")) Color.Green else Color.Red
                Text(text = message, color = messageColor)
            }

            Button(
                onClick = {
                    if (email.isNotEmpty() && token.isNotEmpty()) {
                        profileViewModel.updateEmail(token, email) { isSuccess, responseMessage ->
                            if (isSuccess) {
                                sharedPreferences.edit().putString("USER_EMAIL", email).apply()
                                message = "Success: Email successfully changed"
                                navController.navigate("profile")
                            } else {
                                message = responseMessage
                            }
                        }
                    } else {
                        message = "Email and token are required"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update Email")
            }
        }
    }
}
