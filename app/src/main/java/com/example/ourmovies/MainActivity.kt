package com.example.ourmovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.ourmovies.presentation.FavoriteMoviesScreen

import com.example.ourmovies.presentation.LoginScreen
import com.example.ourmovies.presentation.MainPage
import com.example.ourmovies.presentation.MovieDetailsScreen
import com.example.ourmovies.presentation.navigation.Screen

import com.example.ourmovies.ui.theme.OurMoviesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OurMoviesTheme {
            val authViewModel: AuthViewModel = viewModel()
            Navigation(authViewModel = authViewModel)

            }
        }
    }
}

class AuthViewModel : ViewModel() {
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    fun setToken(newToken: String) {
        _token.value = newToken
    }
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel // Ensure this is passed from a parent (like MainActivity)
) {
    val token by authViewModel.token.observeAsState("")

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController, onLoginSuccess = { newToken ->
                authViewModel.setToken(newToken ?: "") // ✅ No need for .toString()
                navController.navigate(Screen.MainPage.route)
            })
        }


        composable(
            route = "movieDetails/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            MovieDetailsScreen(movieId, navController = navController, authViewModel = authViewModel)
        }

        composable(route = "FavoriteMoviesScreen") {
            FavoriteMoviesScreen(token = token, navController = navController)
        }

        composable(route = Screen.MainPage.route) {
            MainPage(navController = navController)
        }
    }
}


