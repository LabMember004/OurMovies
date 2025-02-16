package com.example.ourmovies.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ourmovies.domain.viewModels.RegisterViewModel

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun Register(navController: NavController, onNavigateBack:() -> Unit) {
    val viewModel: RegisterViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordMatch by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var onSuccessMessage by remember { mutableStateOf("") }

    viewModel.name = name
    viewModel.email = email
    viewModel.password = password
    viewModel.confirmPassword = confirmPassword

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
        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (!isPasswordMatch) {
            Text(text = "Passwords do not match", color = Color.Red)
        }

        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Text(text = "Invalid email format", color = Color.Red)
        }

        Button(
            onClick = {
                if (viewModel.name.isEmpty() || viewModel.email.isEmpty() || viewModel.password.isEmpty() || viewModel.confirmPassword.isEmpty()) {
                    errorMessage = "All fields are required"
                } else {
                    viewModel.registerUser(viewModel.name, viewModel.email, viewModel.password, viewModel.confirmPassword)
                    onSuccessMessage = "Registration Successful"
                    errorMessage = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }

        Text("Already have an account?")

        Text(
            text = "Login",
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier =  Modifier.clickable {
                onNavigateBack()
            }


        )
        if (onSuccessMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text (text = onSuccessMessage , color = Color.Green)
        }
    }
}

