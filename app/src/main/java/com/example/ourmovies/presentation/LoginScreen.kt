package com.example.ourmovies.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ourmovies.domain.viewModels.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: (String) -> Unit , onNavigateToRegister : () -> Unit) {
    val viewModel: LoginViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                errorMessage = ""
                viewModel.loginUser(email, password) { token ->
                    if (token != null) {
                        onLoginSuccess(token) // ✅ Pass token
                    } else {
                        errorMessage = "Login failed"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Text("Dont have an account?")

        Text(
            text = "Register",
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier =  Modifier.clickable {
                onNavigateToRegister()
            }
        )

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        }

        if (viewModel.errorMessage.isNotEmpty()) {
            Text(text = viewModel.errorMessage, color = MaterialTheme.colorScheme.error)
        }

        if (viewModel.token != null) {
            Log.d("LoginScreen", "User Token: ${viewModel.token}")
            Text(text = "Login successful!", color = MaterialTheme.colorScheme.primary)
        }
    }
}