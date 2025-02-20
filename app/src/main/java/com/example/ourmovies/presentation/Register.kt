package com.example.ourmovies.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.util.Patterns
import com.example.ourmovies.domain.viewModels.RegisterViewModel

@Composable
fun Register(navController: NavController, onNavigateBack: () -> Unit) {
    val viewModel: RegisterViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordMatch by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var onSuccessMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

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
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        isPasswordMatch = password == confirmPassword

        if (!isPasswordMatch) {
            Text(text = "Passwords do not match", color = Color.Red)
        }

        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Text(text = "Invalid email format", color = Color.Red)
        }

        Button(
            onClick = {
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    errorMessage = "All fields are required"
                    onSuccessMessage = ""
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    errorMessage = "Invalid email format"
                    onSuccessMessage = ""
                } else if (!isPasswordMatch) {
                    errorMessage = "Passwords do not match"
                    onSuccessMessage = ""
                } else {
                    viewModel.registerUser(name, email, password, confirmPassword)
                    onSuccessMessage = "Registration Successful"
                    errorMessage = ""
                    navController.navigate("home") {
                        popUpTo("register"){inclusive = true}
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }

        if (onSuccessMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = onSuccessMessage, color = Color.Green)
        }

        Text("Already have an account?")

        Text(
            text = "Login",
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onNavigateBack() }
        )
    }
}