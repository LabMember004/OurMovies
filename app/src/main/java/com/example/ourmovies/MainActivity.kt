package com.example.ourmovies

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Movie
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ourmovies.domain.navBar.BottomNavBar
import com.example.ourmovies.domain.network.IsInternetAvailable
import com.example.ourmovies.domain.viewModels.AuthViewModel
import com.example.ourmovies.presentation.FavoriteMoviesScreen

import com.example.ourmovies.presentation.LoginScreen
import com.example.ourmovies.presentation.MainPage
import com.example.ourmovies.presentation.MovieDetailsScreen
import com.example.ourmovies.presentation.Profile
import com.example.ourmovies.presentation.navigation.Screen

import com.example.ourmovies.ui.theme.OurMoviesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OurMoviesTheme {
                Navigation()

            }
        }
    }
    class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!IsInternetAvailable.checkInternetAvailability(context)) {
                Toast.makeText(context, "No internet connection available", Toast.LENGTH_SHORT).show()
            }
        }
    }


    val networkReceiver = NetworkChangeReceiver()

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkReceiver)
    }

}



@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = viewModel()
) {
    val token by viewModel.token.observeAsState("")

    Scaffold(
        bottomBar = {
            if (token.isNotEmpty()) {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (token.isNotEmpty()) Screen.MainPage.route else Screen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screen.Login.route) {
                LoginScreen(navController = navController, onLoginSuccess = { newToken ->
                    viewModel.setToken(newToken ?: "")
                    navController.navigate(Screen.MainPage.route)
                })
            }

            composable(
                route = "movieDetails/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                MovieDetailsScreen(movieId, navController = navController, authViewModel = viewModel)
            }

            composable(route = Screen.FavoriteMovieScreen.route) {
                FavoriteMoviesScreen(token = token, navController = navController)
            }

            composable(route = Screen.MainPage.route) {
                MainPage(navController = navController)
            }
            composable(route=Screen.Profile.route) {
                Profile(navController = navController)
            }
        }
    }
}







