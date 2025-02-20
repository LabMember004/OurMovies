package com.example.ourmovies.presentation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ourmovies.MainActivity
import com.example.ourmovies.domain.viewModels.AuthViewModel
import com.example.ourmovies.domain.viewModels.DeleteViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import com.example.ourmovies.domain.viewModels.ThemeViewModel

@Composable
fun SettingsScreen(
    token: String,
    navController: NavController,
    profileViewModel: DeleteViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    themeViewModel: ThemeViewModel = viewModel()
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

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
            Text(text = "Settings", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { themeViewModel.toggleTheme() }, modifier = Modifier.padding(16.dp)) {
                if (themeViewModel.isDarkMode.value) {
                    Text("Switch to Light Mode")
                } else {
                    Text("Switch to Dark Mode")
                }
            }


            Button(
                onClick = { navController.navigate("updateEmail") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text(text = "Update Email")
            }
            Button(
                onClick = { navController.navigate("updatePassword") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update Password")
            }

            Button(
                onClick = {
                    authViewModel.setToken("")
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Logout")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Profile")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete your profile?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    profileViewModel.deleteProfile(
                        token,
                        onSuccess = {
                            Toast.makeText(context, "Profile deleted", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") { popUpTo("settings") { inclusive = true } }
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}
