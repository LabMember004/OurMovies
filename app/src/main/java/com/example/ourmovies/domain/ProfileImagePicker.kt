package com.example.ourmovies.domain

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileImagePicker(context: Context) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("PROFILE_IMAGE", uri.toString())
                .apply()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val savedImageUri = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("PROFILE_IMAGE", null)

        AsyncImage(
            model = imageUri ?: savedImageUri,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )

        Button(
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text("Choose Profile Image")
        }
    }
}